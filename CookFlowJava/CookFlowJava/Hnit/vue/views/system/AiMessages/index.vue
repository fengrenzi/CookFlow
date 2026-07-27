<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属会话ID" prop="conversationId">
        <el-input
          v-model="queryParams.conversationId"
          placeholder="请输入所属会话ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="sender: user/system/assistant" prop="sender">
        <el-input
          v-model="queryParams.sender"
          placeholder="请输入sender: user/system/assistant"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供计费/分析使用的token数" prop="tokenCount">
        <el-input
          v-model="queryParams.tokenCount"
          placeholder="请输入供计费/分析使用的token数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择消息时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:AiMessages:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:AiMessages:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:AiMessages:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:AiMessages:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="AiMessagesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="消息ID" align="center" prop="id" />
      <el-table-column label="所属会话ID" align="center" prop="conversationId" />
      <el-table-column label="sender: user/system/assistant" align="center" prop="sender" />
      <el-table-column label="文本内容" align="center" prop="content" />
      <el-table-column label="text/audio/image等" align="center" prop="contentType" />
      <el-table-column label="语音识别替换结果、语速、置信度等" align="center" prop="metadata" />
      <el-table-column label="供计费/分析使用的token数" align="center" prop="tokenCount" />
      <el-table-column label="消息时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:AiMessages:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:AiMessages:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改AI 会话消息（按消息存储）对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="AiMessagesRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属会话ID" prop="conversationId">
          <el-input v-model="form.conversationId" placeholder="请输入所属会话ID" />
        </el-form-item>
        <el-form-item label="sender: user/system/assistant" prop="sender">
          <el-input v-model="form.sender" placeholder="请输入sender: user/system/assistant" />
        </el-form-item>
        <el-form-item label="文本内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="供计费/分析使用的token数" prop="tokenCount">
          <el-input v-model="form.tokenCount" placeholder="请输入供计费/分析使用的token数" />
        </el-form-item>
        <el-form-item label="消息时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择消息时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AiMessages">
import { listAiMessages, getAiMessages, delAiMessages, addAiMessages, updateAiMessages } from "@/api/system/AiMessages";

const { proxy } = getCurrentInstance();

const AiMessagesList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    conversationId: null,
    sender: null,
    content: null,
    contentType: null,
    metadata: null,
    tokenCount: null,
    createdAt: null
  },
  rules: {
    conversationId: [
      { required: true, message: "所属会话ID不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询AI 会话消息（按消息存储）列表 */
function getList() {
  loading.value = true;
  listAiMessages(queryParams.value).then(response => {
    AiMessagesList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    conversationId: null,
    sender: null,
    content: null,
    contentType: null,
    metadata: null,
    tokenCount: null,
    createdAt: null
  };
  proxy.resetForm("AiMessagesRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加AI 会话消息（按消息存储）";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getAiMessages(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改AI 会话消息（按消息存储）";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["AiMessagesRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateAiMessages(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addAiMessages(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除AI 会话消息（按消息存储）编号为"' + _ids + '"的数据项？').then(function() {
    return delAiMessages(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/AiMessages/export', {
    ...queryParams.value
  }, `AiMessages_${new Date().getTime()}.xlsx`)
}

getList();
</script>

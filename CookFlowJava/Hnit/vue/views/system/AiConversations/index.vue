<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="发起会话的用户ID，关联 sys_user.user_id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入发起会话的用户ID，关联 sys_user.user_id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会话键，用于前端恢复会话" prop="sessionKey">
        <el-input
          v-model="queryParams.sessionKey"
          placeholder="请输入会话键，用于前端恢复会话"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="使用的模型名" prop="model">
        <el-input
          v-model="queryParams.model"
          placeholder="请输入使用的模型名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型版本/配置" prop="modelVersion">
        <el-input
          v-model="queryParams.modelVersion"
          placeholder="请输入模型版本/配置"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最近活跃时间" prop="lastActiveAt">
        <el-date-picker clearable
          v-model="queryParams.lastActiveAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择最近活跃时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="创建时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择创建时间">
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
          v-hasPermi="['system:AiConversations:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:AiConversations:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:AiConversations:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:AiConversations:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="AiConversationsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会话ID" align="center" prop="id" />
      <el-table-column label="发起会话的用户ID，关联 sys_user.user_id" align="center" prop="userId" />
      <el-table-column label="会话键，用于前端恢复会话" align="center" prop="sessionKey" />
      <el-table-column label="使用的模型名" align="center" prop="model" />
      <el-table-column label="模型版本/配置" align="center" prop="modelVersion" />
      <el-table-column label="会话额外上下文" align="center" prop="context" />
      <el-table-column label="会话摘要/要点" align="center" prop="summary" />
      <el-table-column label="最近活跃时间" align="center" prop="lastActiveAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.lastActiveAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:AiConversations:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:AiConversations:remove']">删除</el-button>
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

    <!-- 添加或修改AI 会话元信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="AiConversationsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发起会话的用户ID，关联 sys_user.user_id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入发起会话的用户ID，关联 sys_user.user_id" />
        </el-form-item>
        <el-form-item label="会话键，用于前端恢复会话" prop="sessionKey">
          <el-input v-model="form.sessionKey" placeholder="请输入会话键，用于前端恢复会话" />
        </el-form-item>
        <el-form-item label="使用的模型名" prop="model">
          <el-input v-model="form.model" placeholder="请输入使用的模型名" />
        </el-form-item>
        <el-form-item label="模型版本/配置" prop="modelVersion">
          <el-input v-model="form.modelVersion" placeholder="请输入模型版本/配置" />
        </el-form-item>
        <el-form-item label="会话摘要/要点" prop="summary">
          <el-input v-model="form.summary" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="最近活跃时间" prop="lastActiveAt">
          <el-date-picker clearable
            v-model="form.lastActiveAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择最近活跃时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
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

<script setup name="AiConversations">
import { listAiConversations, getAiConversations, delAiConversations, addAiConversations, updateAiConversations } from "@/api/system/AiConversations";

const { proxy } = getCurrentInstance();

const AiConversationsList = ref([]);
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
    userId: null,
    sessionKey: null,
    model: null,
    modelVersion: null,
    context: null,
    summary: null,
    lastActiveAt: null,
    createdAt: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询AI 会话元信息列表 */
function getList() {
  loading.value = true;
  listAiConversations(queryParams.value).then(response => {
    AiConversationsList.value = response.rows;
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
    userId: null,
    sessionKey: null,
    model: null,
    modelVersion: null,
    context: null,
    summary: null,
    lastActiveAt: null,
    createdAt: null
  };
  proxy.resetForm("AiConversationsRef");
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
  title.value = "添加AI 会话元信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getAiConversations(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改AI 会话元信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["AiConversationsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateAiConversations(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addAiConversations(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除AI 会话元信息编号为"' + _ids + '"的数据项？').then(function() {
    return delAiConversations(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/AiConversations/export', {
    ...queryParams.value
  }, `AiConversations_${new Date().getTime()}.xlsx`)
}

getList();
</script>

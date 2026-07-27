<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['system:IngredientDetails:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:IngredientDetails:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:IngredientDetails:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:IngredientDetails:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="IngredientDetailsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="食材ID" align="center" prop="ingredientId" />
      <el-table-column label="轮播图数据" align="center" prop="carousel" />
      <el-table-column label="挑选提示" align="center" prop="selectionTips" />
      <el-table-column label="处理步骤" align="center" prop="processingSteps" />
      <el-table-column label="营养信息" align="center" prop="nutrition" />
      <el-table-column label="科普知识点" align="center" prop="knowledgePoints" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:IngredientDetails:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:IngredientDetails:remove']">删除</el-button>
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

    <!-- 添加或修改食材详情静态内容对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="IngredientDetailsRef" :model="form" :rules="rules" label-width="80px">
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

<script setup name="IngredientDetails">
import { listIngredientDetails, getIngredientDetails, delIngredientDetails, addIngredientDetails, updateIngredientDetails } from "@/api/system/IngredientDetails";

const { proxy } = getCurrentInstance();

const IngredientDetailsList = ref([]);
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
    carousel: null,
    selectionTips: null,
    processingSteps: null,
    nutrition: null,
    knowledgePoints: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询食材详情静态内容列表 */
function getList() {
  loading.value = true;
  listIngredientDetails(queryParams.value).then(response => {
    IngredientDetailsList.value = response.rows;
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
    ingredientId: null,
    carousel: null,
    selectionTips: null,
    processingSteps: null,
    nutrition: null,
    knowledgePoints: null
  };
  proxy.resetForm("IngredientDetailsRef");
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
  ids.value = selection.map(item => item.ingredientId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加食材详情静态内容";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _ingredientId = row.ingredientId || ids.value
  getIngredientDetails(_ingredientId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改食材详情静态内容";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["IngredientDetailsRef"].validate(valid => {
    if (valid) {
      if (form.value.ingredientId != null) {
        updateIngredientDetails(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addIngredientDetails(form.value).then(response => {
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
  const _ingredientIds = row.ingredientId || ids.value;
  proxy.$modal.confirm('是否确认删除食材详情静态内容编号为"' + _ingredientIds + '"的数据项？').then(function() {
    return delIngredientDetails(_ingredientIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/IngredientDetails/export', {
    ...queryParams.value
  }, `IngredientDetails_${new Date().getTime()}.xlsx`)
}

getList();
</script>

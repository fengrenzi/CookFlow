<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属菜谱ID" prop="recipeId">
        <el-input
          v-model="queryParams.recipeId"
          placeholder="请输入所属菜谱ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="步骤顺序编号，从0或1开始" prop="stepIndex">
        <el-input
          v-model="queryParams.stepIndex"
          placeholder="请输入步骤顺序编号，从0或1开始"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['system:RecipeSteps:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:RecipeSteps:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:RecipeSteps:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:RecipeSteps:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="RecipeStepsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="步骤ID，自增" align="center" prop="id" />
      <el-table-column label="所属菜谱ID" align="center" prop="recipeId" />
      <el-table-column label="步骤顺序编号，从0或1开始" align="center" prop="stepIndex" />
      <el-table-column label="步骤内容" align="center" prop="content" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:RecipeSteps:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:RecipeSteps:remove']">删除</el-button>
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

    <!-- 添加或修改菜谱步骤（有序）对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="RecipeStepsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属菜谱ID" prop="recipeId">
          <el-input v-model="form.recipeId" placeholder="请输入所属菜谱ID" />
        </el-form-item>
        <el-form-item label="步骤顺序编号，从0或1开始" prop="stepIndex">
          <el-input v-model="form.stepIndex" placeholder="请输入步骤顺序编号，从0或1开始" />
        </el-form-item>
        <el-form-item label="步骤内容">
          <editor v-model="form.content" :min-height="192"/>
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

<script setup name="RecipeSteps">
import { listRecipeSteps, getRecipeSteps, delRecipeSteps, addRecipeSteps, updateRecipeSteps } from "@/api/system/RecipeSteps";

const { proxy } = getCurrentInstance();

const RecipeStepsList = ref([]);
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
    recipeId: null,
    stepIndex: null,
    content: null
  },
  rules: {
    recipeId: [
      { required: true, message: "所属菜谱ID不能为空", trigger: "blur" }
    ],
    stepIndex: [
      { required: true, message: "步骤顺序编号，从0或1开始不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询菜谱步骤（有序）列表 */
function getList() {
  loading.value = true;
  listRecipeSteps(queryParams.value).then(response => {
    RecipeStepsList.value = response.rows;
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
    recipeId: null,
    stepIndex: null,
    content: null
  };
  proxy.resetForm("RecipeStepsRef");
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
  title.value = "添加菜谱步骤（有序）";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getRecipeSteps(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改菜谱步骤（有序）";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["RecipeStepsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateRecipeSteps(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addRecipeSteps(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除菜谱步骤（有序）编号为"' + _ids + '"的数据项？').then(function() {
    return delRecipeSteps(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/RecipeSteps/export', {
    ...queryParams.value
  }, `RecipeSteps_${new Date().getTime()}.xlsx`)
}

getList();
</script>

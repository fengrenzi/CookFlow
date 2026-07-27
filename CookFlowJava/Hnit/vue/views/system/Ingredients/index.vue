<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="食材名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入食材名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="食材图片URL" prop="imgUrl">
        <el-input
          v-model="queryParams.imgUrl"
          placeholder="请输入食材图片URL"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="食材分类ID或名称" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入食材分类ID或名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字母分组" prop="letter">
        <el-input
          v-model="queryParams.letter"
          placeholder="请输入字母分组"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="默认计量单位" prop="unit">
        <el-input
          v-model="queryParams.unit"
          placeholder="请输入默认计量单位"
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
          v-hasPermi="['system:Ingredients:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:Ingredients:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:Ingredients:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:Ingredients:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="IngredientsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="食材ID，自定义或组合ID" align="center" prop="id" />
      <el-table-column label="食材名称" align="center" prop="name" />
      <el-table-column label="食材图片URL" align="center" prop="imgUrl" />
      <el-table-column label="食材分类ID或名称" align="center" prop="category" />
      <el-table-column label="字母分组" align="center" prop="letter" />
      <el-table-column label="默认计量单位" align="center" prop="unit" />
      <el-table-column label="扩展字段，存放额外元信息" align="center" prop="extra" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:Ingredients:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:Ingredients:remove']">删除</el-button>
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

    <!-- 添加或修改食材对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="IngredientsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="食材名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入食材名称" />
        </el-form-item>
        <el-form-item label="食材图片URL" prop="imgUrl">
          <el-input v-model="form.imgUrl" placeholder="请输入食材图片URL" />
        </el-form-item>
        <el-form-item label="食材分类ID或名称" prop="category">
          <el-input v-model="form.category" placeholder="请输入食材分类ID或名称" />
        </el-form-item>
        <el-form-item label="字母分组" prop="letter">
          <el-input v-model="form.letter" placeholder="请输入字母分组" />
        </el-form-item>
        <el-form-item label="默认计量单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入默认计量单位" />
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

<script setup name="Ingredients">
import { listIngredients, getIngredients, delIngredients, addIngredients, updateIngredients } from "@/api/system/Ingredients";

const { proxy } = getCurrentInstance();

const IngredientsList = ref([]);
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
    name: null,
    imgUrl: null,
    category: null,
    letter: null,
    unit: null,
    extra: null
  },
  rules: {
    name: [
      { required: true, message: "食材名称不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询食材列表 */
function getList() {
  loading.value = true;
  listIngredients(queryParams.value).then(response => {
    IngredientsList.value = response.rows;
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
    name: null,
    imgUrl: null,
    category: null,
    letter: null,
    unit: null,
    extra: null
  };
  proxy.resetForm("IngredientsRef");
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
  title.value = "添加食材";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getIngredients(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改食材";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["IngredientsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateIngredients(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addIngredients(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除食材编号为"' + _ids + '"的数据项？').then(function() {
    return delIngredients(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/Ingredients/export', {
    ...queryParams.value
  }, `Ingredients_${new Date().getTime()}.xlsx`)
}

getList();
</script>

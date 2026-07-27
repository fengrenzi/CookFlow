<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="菜谱名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入菜谱名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="菜谱分类" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入菜谱分类"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="准备时间" prop="prepTime">
        <el-input
          v-model="queryParams.prepTime"
          placeholder="请输入准备时间"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="烹饪时间" prop="cookTime">
        <el-input
          v-model="queryParams.cookTime"
          placeholder="请输入烹饪时间"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建者用户ID，关联 sys_user.user_id" prop="createdBy">
        <el-input
          v-model="queryParams.createdBy"
          placeholder="请输入创建者用户ID，关联 sys_user.user_id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间" prop="updatedAt">
        <el-date-picker clearable
          v-model="queryParams.updatedAt"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择更新时间">
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
          v-hasPermi="['system:Recipes:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:Recipes:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:Recipes:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:Recipes:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="RecipesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="菜谱ID，自增主键" align="center" prop="id" />
      <el-table-column label="菜谱名称" align="center" prop="name" />
      <el-table-column label="菜谱描述/正文" align="center" prop="description" />
      <el-table-column label="主图URL" align="center" prop="image" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="菜谱分类" align="center" prop="category" />
      <el-table-column label="准备时间" align="center" prop="prepTime" />
      <el-table-column label="烹饪时间" align="center" prop="cookTime" />
      <el-table-column label="创建者用户ID，关联 sys_user.user_id" align="center" prop="createdBy" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:Recipes:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:Recipes:remove']">删除</el-button>
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

    <!-- 添加或修改菜谱对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="RecipesRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="菜谱名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜谱名称" />
        </el-form-item>
        <el-form-item label="菜谱描述/正文" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="主图URL" prop="image">
          <image-upload v-model="form.image"/>
        </el-form-item>
        <el-form-item label="菜谱分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入菜谱分类" />
        </el-form-item>
        <el-form-item label="准备时间" prop="prepTime">
          <el-input v-model="form.prepTime" placeholder="请输入准备时间" />
        </el-form-item>
        <el-form-item label="烹饪时间" prop="cookTime">
          <el-input v-model="form.cookTime" placeholder="请输入烹饪时间" />
        </el-form-item>
        <el-form-item label="创建者用户ID，关联 sys_user.user_id" prop="createdBy">
          <el-input v-model="form.createdBy" placeholder="请输入创建者用户ID，关联 sys_user.user_id" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="updatedAt">
          <el-date-picker clearable
            v-model="form.updatedAt"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择更新时间">
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

<script setup name="Recipes">
import { listRecipes, getRecipes, delRecipes, addRecipes, updateRecipes } from "@/api/system/Recipes";

const { proxy } = getCurrentInstance();

const RecipesList = ref([]);
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
    description: null,
    image: null,
    category: null,
    prepTime: null,
    cookTime: null,
    createdBy: null,
    createdAt: null,
    updatedAt: null
  },
  rules: {
    name: [
      { required: true, message: "菜谱名称不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询菜谱列表 */
function getList() {
  loading.value = true;
  listRecipes(queryParams.value).then(response => {
    RecipesList.value = response.rows;
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
    description: null,
    image: null,
    category: null,
    prepTime: null,
    cookTime: null,
    createdBy: null,
    createdAt: null,
    updatedAt: null
  };
  proxy.resetForm("RecipesRef");
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
  title.value = "添加菜谱";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getRecipes(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改菜谱";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["RecipesRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateRecipes(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addRecipes(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除菜谱编号为"' + _ids + '"的数据项？').then(function() {
    return delRecipes(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/Recipes/export', {
    ...queryParams.value
  }, `Recipes_${new Date().getTime()}.xlsx`)
}

getList();
</script>

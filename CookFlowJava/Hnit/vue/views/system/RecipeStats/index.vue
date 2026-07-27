<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="浏览量" prop="views">
        <el-input
          v-model="queryParams.views"
          placeholder="请输入浏览量"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="点赞数" prop="likes">
        <el-input
          v-model="queryParams.likes"
          placeholder="请输入点赞数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收藏数" prop="favorites">
        <el-input
          v-model="queryParams.favorites"
          placeholder="请输入收藏数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论数" prop="comments">
        <el-input
          v-model="queryParams.comments"
          placeholder="请输入评论数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="综合热度分" prop="score">
        <el-input
          v-model="queryParams.score"
          placeholder="请输入综合热度分"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="统计更新时间" prop="lastUpdated">
        <el-date-picker clearable
          v-model="queryParams.lastUpdated"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择统计更新时间">
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
          v-hasPermi="['system:RecipeStats:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:RecipeStats:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:RecipeStats:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:RecipeStats:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="RecipeStatsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="菜谱ID" align="center" prop="recipeId" />
      <el-table-column label="浏览量" align="center" prop="views" />
      <el-table-column label="点赞数" align="center" prop="likes" />
      <el-table-column label="收藏数" align="center" prop="favorites" />
      <el-table-column label="评论数" align="center" prop="comments" />
      <el-table-column label="综合热度分" align="center" prop="score" />
      <el-table-column label="统计更新时间" align="center" prop="lastUpdated" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.lastUpdated, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:RecipeStats:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:RecipeStats:remove']">删除</el-button>
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

    <!-- 添加或修改菜谱聚合统计，用于排行榜对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="RecipeStatsRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="浏览量" prop="views">
          <el-input v-model="form.views" placeholder="请输入浏览量" />
        </el-form-item>
        <el-form-item label="点赞数" prop="likes">
          <el-input v-model="form.likes" placeholder="请输入点赞数" />
        </el-form-item>
        <el-form-item label="收藏数" prop="favorites">
          <el-input v-model="form.favorites" placeholder="请输入收藏数" />
        </el-form-item>
        <el-form-item label="评论数" prop="comments">
          <el-input v-model="form.comments" placeholder="请输入评论数" />
        </el-form-item>
        <el-form-item label="综合热度分" prop="score">
          <el-input v-model="form.score" placeholder="请输入综合热度分" />
        </el-form-item>
        <el-form-item label="统计更新时间" prop="lastUpdated">
          <el-date-picker clearable
            v-model="form.lastUpdated"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择统计更新时间">
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

<script setup name="RecipeStats">
import { listRecipeStats, getRecipeStats, delRecipeStats, addRecipeStats, updateRecipeStats } from "@/api/system/RecipeStats";

const { proxy } = getCurrentInstance();

const RecipeStatsList = ref([]);
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
    views: null,
    likes: null,
    favorites: null,
    comments: null,
    score: null,
    lastUpdated: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询菜谱聚合统计，用于排行榜列表 */
function getList() {
  loading.value = true;
  listRecipeStats(queryParams.value).then(response => {
    RecipeStatsList.value = response.rows;
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
    recipeId: null,
    views: null,
    likes: null,
    favorites: null,
    comments: null,
    score: null,
    lastUpdated: null
  };
  proxy.resetForm("RecipeStatsRef");
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
  ids.value = selection.map(item => item.recipeId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加菜谱聚合统计，用于排行榜";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _recipeId = row.recipeId || ids.value
  getRecipeStats(_recipeId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改菜谱聚合统计，用于排行榜";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["RecipeStatsRef"].validate(valid => {
    if (valid) {
      if (form.value.recipeId != null) {
        updateRecipeStats(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addRecipeStats(form.value).then(response => {
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
  const _recipeIds = row.recipeId || ids.value;
  proxy.$modal.confirm('是否确认删除菜谱聚合统计，用于排行榜编号为"' + _recipeIds + '"的数据项？').then(function() {
    return delRecipeStats(_recipeIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/RecipeStats/export', {
    ...queryParams.value
  }, `RecipeStats_${new Date().getTime()}.xlsx`)
}

getList();
</script>

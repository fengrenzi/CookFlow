<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属用户ID，关联 sys_user.user_id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入所属用户ID，关联 sys_user.user_id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联的 recipe id 或 book id" prop="relatedId">
        <el-input
          v-model="queryParams.relatedId"
          placeholder="请输入关联的 recipe id 或 book id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数量" prop="quantity">
        <el-input
          v-model="queryParams.quantity"
          placeholder="请输入数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="添加时间" prop="addedAt">
        <el-date-picker clearable
          v-model="queryParams.addedAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择添加时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:CartItems:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:CartItems:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:CartItems:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:CartItems:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="CartItemsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="购物项ID" align="center" prop="id" />
      <el-table-column label="所属用户ID，关联 sys_user.user_id" align="center" prop="userId" />
      <el-table-column label="项类型：菜谱或书中菜谱" align="center" prop="type" />
      <el-table-column label="关联的 recipe id 或 book id" align="center" prop="relatedId" />
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="可选：具体食材列表" align="center" prop="ingredients" />
      <el-table-column label="添加时间" align="center" prop="addedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.addedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:CartItems:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:CartItems:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改购物车项对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属用户ID，关联 sys_user.user_id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入所属用户ID，关联 sys_user.user_id" />
        </el-form-item>
        <el-form-item label="关联的 recipe id 或 book id" prop="relatedId">
          <el-input v-model="form.relatedId" placeholder="请输入关联的 recipe id 或 book id" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input v-model="form.quantity" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="添加时间" prop="addedAt">
          <el-date-picker clearable
            v-model="form.addedAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择添加时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCartItems, getCartItems, delCartItems, addCartItems, updateCartItems } from "@/api/system/CartItems";

export default {
  name: "CartItems",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 购物车项表格数据
      CartItemsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        type: null,
        relatedId: null,
        quantity: null,
        ingredients: null,
        addedAt: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "所属用户ID，关联 sys_user.user_id不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "项类型：菜谱或书中菜谱不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询购物车项列表 */
    getList() {
      this.loading = true;
      listCartItems(this.queryParams).then(response => {
        this.CartItemsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        type: null,
        relatedId: null,
        quantity: null,
        ingredients: null,
        addedAt: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加购物车项";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCartItems(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改购物车项";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCartItems(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCartItems(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除购物车项编号为"' + ids + '"的数据项？').then(function() {
        return delCartItems(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/CartItems/export', {
        ...this.queryParams
      }, `CartItems_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

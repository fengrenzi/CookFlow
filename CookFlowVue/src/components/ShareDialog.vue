<template>
  <el-dialog v-model="visible" title="发布分享" width="600px" @close="resetForm">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="10-50字" maxlength="50" show-word-limit />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model="form.content" type="textarea" rows="6" placeholder="50-1000字" maxlength="1000" show-word-limit />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio value="image">图片</el-radio>
          <el-radio value="video">视频</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.type === 'video'" label="视频链接" prop="videoUrl">
        <el-input v-model="form.videoUrl" placeholder="请输入视频链接" />
      </el-form-item>
      <el-form-item label="图片上传" v-if="form.type === 'image'">
        <el-upload
          action="/api/upload/image"
          :headers="{ Authorization: `Bearer ${localStorage.getItem('token')}` }"
          :on-success="handleUploadSuccess"
          :on-remove="handleRemove"
          list-type="picture-card"
          :limit="9"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
        <div class="tip">支持jpg/png，最多9张</div>
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-select v-model="form.category">
          <el-option label="烹饪技巧" value="cooking" />
          <el-option label="菜谱分享" value="recipe" />
          <el-option label="甜点烘焙" value="dessert" />
          <el-option label="饮品制作" value="drink" />
          <el-option label="餐厅推荐" value="restaurant" />
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="tagsText">
        <el-input v-model="form.tagsText" placeholder="用逗号分隔，如：家常菜,简单" />
      </el-form-item>
      <el-form-item label="难度" prop="difficulty">
        <el-radio-group v-model="form.difficulty">
          <el-radio value="easy">简单</el-radio>
          <el-radio value="medium">中等</el-radio>
          <el-radio value="hard">困难</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="公开" prop="isPublic">
        <el-switch v-model="form.isPublic" active-text="公开" inactive-text="私密" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submit">发布</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { createShare } from '@/api/forum'

const props = defineProps<{ modelValue: boolean }>()
const emit = defineEmits(['update:modelValue', 'success'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref()
const submitting = ref(false)
const form = reactive({
  title: '',
  content: '',
  type: 'image',
  videoUrl: '',
  category: '',
  tagsText: '',
  difficulty: '',
  isPublic: true,
  imageIds: [] as string[]
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }, { min: 10, max: 50, message: '10-50字' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }, { min: 50, max: 1000, message: '50-1000字' }],
  type: [{ required: true, message: '请选择类型' }],
  videoUrl: [{ required: true, message: '请输入视频链接' }],
  category: [{ required: true, message: '请选择分类' }],
  difficulty: [{ required: true, message: '请选择难度' }]
}

const handleUploadSuccess = (res: any) => {
  form.imageIds.push(res.data.id)
}
const handleRemove = (file: any) => {
  const idx = form.imageIds.findIndex(id => id === file.id)
  if (idx !== -1) form.imageIds.splice(idx, 1)
}
const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, { title: '', content: '', type: 'image', videoUrl: '', category: '', tagsText: '', difficulty: '', isPublic: true, imageIds: [] })
}
const submit = async () => {
  await formRef.value.validate()
  const tags = form.tagsText.split(',').map(t => t.trim()).filter(t => t)
  if (tags.length > 5) return ElMessage.warning('标签最多5个')
  submitting.value = true
  try {
    await createShare({
      title: form.title,
      content: form.content,
      type: form.type,
      videoUrl: form.videoUrl,
      category: form.category,
      tags,
      difficulty: form.difficulty,
      isPublic: form.isPublic,
      imageIds: form.imageIds
    })
    ElMessage.success('发布成功')
    visible.value = false
    emit('success')
  } catch {
    ElMessage.error('发布失败')
  } finally {
    submitting.value = false
  }
}
</script>
<style scoped>
.tip { font-size: 12px; color: #999; margin-top: 4px; }
</style>
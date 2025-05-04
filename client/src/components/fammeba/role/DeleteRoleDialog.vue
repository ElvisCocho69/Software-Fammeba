<script setup>
import { ref } from 'vue'
import { $api } from '@/utils/api'

const props = defineProps({
  isDialogVisible: {
    type: Boolean,
    required: true,
  },
  role: {
    type: Object,
    required: false,
    default: null
  }
})

const emit = defineEmits(['update:isDialogVisible', 'role-deleted'])

const loading = ref(false)
const error = ref(null)

const closeDialog = () => {
  emit('update:isDialogVisible', false)
  error.value = null
}

const confirmDelete = async () => {
  loading.value = true
  error.value = null
  try {
    await $api(`/roles/${props.role.id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    emit('role-deleted')
    closeDialog()
  } catch (e) {
    error.value = 'No se pudo eliminar el rol.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <VDialog :model-value="props.isDialogVisible" max-width="400" @update:model-value="closeDialog">
    <template v-if="role">
      <VCard>
        <VCardTitle class="text-h6 text-center">
          ¿Eliminar rol?
        </VCardTitle>
        <VCardText class="text-center">
          ¿Estás seguro que deseas eliminar el rol <b>{{ role.name }}</b>?<br>
          Esta acción no se puede deshacer.
          <VAlert v-if="error" type="error" class="mt-3">{{ error }}</VAlert>
        </VCardText>
        <VCardActions class="justify-center">
          <VBtn color="error" variant="tonal" :loading="loading" @click="confirmDelete">Eliminar</VBtn>
          <VBtn variant="tonal" @click="closeDialog">Cancelar</VBtn>
        </VCardActions>
      </VCard>
    </template>
  </VDialog>
</template> 
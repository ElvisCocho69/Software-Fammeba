<script setup>
import { $api } from '@/utils/api'
import { ref, watch } from 'vue'

const loading = ref(false)

const props = defineProps({
  isDialogVisible: {
    type: Boolean,
    required: true,
  },
  userData: {
    type: Object,
    required: false,
    default: () => ({})
  }
})

const emit = defineEmits(['update:isDialogVisible', 'user-deleted'])

const dialogVisibleUpdate = val => {
  emit('update:isDialogVisible', val)
}

const handleCloseDialog = () => {
 emit('update:isDialogVisible', false)
 user.value = null
 warning.value = null
 error_exists.value = null
 success.value = null
}

const user = ref(null)
const warning = ref(null)
const error_exists = ref(null)
const success = ref(null)

// Función para verificar si el usuario existe (excluyendo el usuario actual)
const checkUserExists = async (username) => {
  try {
    const resp = await $api('/users', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    const users = resp.content || []
    return users.some(existingUser => 
      existingUser.id !== props.userData.id &&
      existingUser.username.toLowerCase() === username.toLowerCase()
    )
  } catch (error) {
    console.error('Error al verificar usuario existente:', error)
    return false
  }
}

// Cuando se abre el diálogo, cargar los datos del usuario
watch(
  [() => props.userData, () => props.isDialogVisible],
  ([newUser, isVisible]) => {
    if (isVisible && newUser && newUser.id) {
      user.value = newUser.username
    }
    if (!isVisible) {
      user.value = null
    }
  },
  { immediate: true }
)

// Función para confirmar la eliminación del usuario
const confirmDelete = async () => {
  warning.value = null
  error_exists.value = null
  success.value = null

  // Verificar si el usuario existe (excluyendo el usuario actual)
  const userExists = await checkUserExists(user.value);
  if (userExists) {
    error_exists.value = `El usuario "${user.value}" ya existe. Por favor, elija otro nombre.`;
    return;
  }

  const closeDialog = () => {
  emit('update:isDialogVisible', false)
  error.value = null
}

  let data = {
    status: 'DISABLED'
  }
  
  try {
    await $api(`/users/${props.userData.id}/status`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      body: data
    })
    success.value = "Usuario eliminado correctamente"
    emit('user-deleted')

    setTimeout(() => {
      dialogVisibleUpdate(false)
      success.value = null
      warning.value = null
      
    }, 1500)
  } catch (error) {
    console.error('Error al eliminar usuario:', error)
    if (error.response?._data?.backendMessage) {
      error_exists.value = error.response._data.backendMessage
    } else if (error.response?.status === 403) {
      error_exists.value = "No tiene permisos para eliminar usuarios"
    } else {
      error_exists.value = "Hubo un problema al eliminar el usuario. Por favor, intente nuevamente."
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
    <VDialog
      :model-value="props.isDialogVisible"
      persistent
      max-width="450px"
      @update:model-value="closeDialog"
    >
      <template v-if="user">
        <VCard :title="'¿Eliminar usuario?'">
          <DialogCloseBtn
            variant="text"
            size="default"
            @click="handleCloseDialog"
          />
          <VCardText>
            ¿Estás seguro que deseas eliminar el usuario <b>{{ user }}</b>?<br>
            <strong>Esta acción no se puede deshacer.</strong>
            <VAlert v-if="error" type="error" class="mt-3">{{ error }}</VAlert>
          </VCardText>
  
          <VCardText class="d-flex justify-end flex-wrap gap-4">
            <VBtn
              color="error"
              variant="tonal"
              :loading="loading"
              @click="confirmDelete"
              prepend-icon="ri-delete-bin-6-fill"
            >
              Eliminar
            </VBtn>
            <VBtn
              variant="tonal"
              @click="handleCloseDialog"
              prepend-icon="ri-close-circle-fill"
            >
              Cancelar
            </VBtn>
          </VCardText>
        </VCard>
      </template>
    </VDialog>
  </template>
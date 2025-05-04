<script setup>
import { $api } from '@/utils/api'
import { PERMISOS } from '@/utils/constants'
import { ref, watch } from 'vue'

const props = defineProps({
  isDialogVisible: {
    type: Boolean,
    required: true,
  },
  roleData: {
    type: Object,
    required: false,
    default: () => ({})
  }
})

const emit = defineEmits(['update:isDialogVisible', 'role-updated'])

const dialogVisibleUpdate = val => {
  emit('update:isDialogVisible', val)
}

const handleCloseDialog = () => {
  emit('update:isDialogVisible', false)
  role.value = null
  permissions.value = []
  warning.value = null
  error_exists.value = null
  success.value = null
}

const LIST_PERMISSION = PERMISOS

const role = ref(null)
const permissions = ref([])
const warning = ref(null)
const error_exists = ref(null)
const success = ref(null)

// Cuando se abre el diálogo, cargar los datos del rol
watch(
  [() => props.roleData, () => props.isDialogVisible],
  ([newRole, isVisible]) => {
    if (isVisible && newRole && newRole.id) {
      role.value = newRole.name
      permissions.value = newRole.permissions.map(perm => {
        for (const module of LIST_PERMISSION) {
          for (const permiso of module.permisos) {
            if (permiso.permiso === perm) {
              return permiso.operationId
            }
          }
        }
        return null
      }).filter(id => id !== null)
    }
    if (!isVisible) {
      role.value = null
      permissions.value = []
    }
  },
  { immediate: true }
)

const update = async () => {
  warning.value = null
  error_exists.value = null
  success.value = null

  if (!role.value) {
    warning.value = "Se debe llenar el nombre del rol"
    return
  }
  if (permissions.value.length == 0) {
    warning.value = "Se debe seleccionar al menos un permiso"
    return
  }

  let data = {
    name: role.value,
    operationIds: permissions.value,
  }

  try {
    const resp = await $api(`/roles/${props.roleData.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: data
    })

    console.log(resp)
    success.value = "Rol actualizado correctamente"
    emit('role-updated')

    setTimeout(() => {
      dialogVisibleUpdate(false)
      success.value = null
      warning.value = null
      role.value = null
      permissions.value = []
    }, 1500)

  } catch (error) {
    console.error("Error al actualizar el rol:", error)
    if (error.response?._data?.backendMessage) {
      error_exists.value = error.response._data.backendMessage
    } else if (error.response?.status === 403) {
      error_exists.value = "No tiene permisos para actualizar roles"
    } else {
      error_exists.value = "Hubo un problema al actualizar el rol."
    }
  }
}
</script>

<template>
  <VDialog :model-value="props.isDialogVisible" max-width="750" @update:model-value="dialogVisibleUpdate">
    <VCard class="refer-and-earn-dialog pa-3 pa-sm-11">
      <!-- 👉 dialog close btn -->
      <DialogCloseBtn variant="text" size="default" @click="handleCloseDialog" />

      <VCardText class="pa-5">
        <div class="mb-6">
          <h4 class="text-h4 text-center mb-2">
            Editar Rol
          </h4>
        </div>

        <VTextField label="Rol" v-model="role" placeholder="Ejemplo: Administrador" />

        <VAlert type="warning" class="mt-3" v-if="warning" closable>
          <strong>{{ warning }}</strong>
        </VAlert>

        <VAlert type="error" class="mt-3" v-if="error_exists" closable>
          <strong>Error al actualizar rol</strong>
        </VAlert>

        <VAlert type="success" class="mt-3" v-if="success" closable>
          <strong>{{ success }}</strong>
        </VAlert>
      </VCardText>

      <VCardText class="pa-5">
        <VBtn color="primary mb-4" @click="update">
          Actualizar
        </VBtn>
        <VTable>
          <thead>
            <tr>
              <th class="text-uppercase">
                Módulo
              </th>
              <th class="text-uppercase">
                Permiso
              </th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="(item, index) in LIST_PERMISSION" :key="index">
              <td>
                {{ item.name }}
              </td>
              <td>
                <ul>
                  <li v-for="(permiso, index2) in item.permisos" :key="index2" style="list-style: none;">
                    <VCheckbox
                      :label="permiso.name"
                      :value="permiso.operationId"
                      v-model="permissions"
                    />
                  </li>
                </ul>
              </td>
            </tr>
          </tbody>
        </VTable>
      </VCardText>
    </VCard>
  </VDialog>
</template>

<style lang="scss">
.refer-link-input {
  .v-field--appended {
    padding-inline-end: 0;
  }

  .v-field__append-inner {
    padding-block-start: 0.125rem;
  }
}
</style> 
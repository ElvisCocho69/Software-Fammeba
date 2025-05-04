<script setup>
import { ref } from 'vue'
import data from '@/views/js/datatable'

const selectedRole = ref()
const selectedStatus = ref()

const headers = [
  { title: 'NOMBRE', key: 'fullName' },
  { title: 'USUARIO', key: 'email' },
  { title: 'ROL', key: 'post' },
  { title: 'ESTADO', key: 'status' },
  { title: 'ACCIONES', key: 'actions', sortable: false },
]

const resolveStatusVariant = status => {
  if (status === 'Activo')
    return { color: 'primary', text: 'Activo' }
  else if (status === 'Inactivo')
    return { color: 'error', text: 'Inactivo' }
  else
    return { color: 'info', text: 'Desconocido' }
}


const roles = ['Administrador', 'Recepcionista', 'Operario']
const status = ['Activo', 'Inactivo']
</script>

<template>

    <VCard class="mb-6">

      <VCardItem class="pb-4">
        <VCardTitle>Empleados</VCardTitle>
      </VCardItem>
      <VCardText>
        <VRow>
          <VCol cols="12" sm="4">
            <VSelect v-model="selectedRole" :items="roles" label="Seleccionar Rol" placeholder="Seleccionar Rol"
              clearable clear-icon="ri-close-line" />
          </VCol>

          <VCol cols="12" sm="4">
            <VSelect v-model="selectedStatus" :items="status" label="Seleccionar Estado"
              placeholder="Seleccionar Estado" clearable clear-icon="ri-close-line" />
          </VCol>
        </VRow>
      </VCardText>

      <VDivider />

      <VCardText>
        <VRow class="align-center">
          <!-- Botón Exportar -->
          <VCol cols="12" sm="6" class="d-flex">
            <VBtn variant="outlined" color="secondary" prepend-icon="ri-upload-2-line">
              Exportar
            </VBtn>
          </VCol>

          <!-- Buscar + Add New User -->
          <VCol cols="12" sm="6" class="d-flex justify-end align-center gap-4">
            <VTextField placeholder="Buscar Empleado" density="compact" style="max-width: 200px;" />
            <VBtn @click="isAddNewUserDrawerVisible = true">
              Añadir Empleado
            </VBtn>
          </VCol>
        </VRow>
      </VCardText>

      <VDataTable
    :headers="headers"
    :items="data"
    :items-per-page="5"
    class="text-no-wrap"
  >
    <!-- full name -->
    <template #item.fullName="{ item }">
      <div class="d-flex align-center">
        <VAvatar
          size="32"
          :color="item.avatar ? '' : 'primary'"
          :class="item.avatar ? '' : 'v-avatar-light-bg primary--text'"
          :variant="!item.avatar ? 'tonal' : undefined"
        >
          <VImg
            v-if="item.avatar"
            :src="item.avatar"
          />
          <span
            v-else
            class="text-sm"
          >{{ avatarText(item.fullName) }}</span>
        </VAvatar>
        <div class="d-flex flex-column ms-3">
          <span class="d-block font-weight-medium text-high-emphasis text-truncate">{{ item.fullName }}</span>
          <small>{{ item.post }}</small>
        </div>
      </div>
    </template>

    <template #item.status="{ item }">
      <VChip
        :color="resolveStatusVariant(item.status).color"
        class="font-weight-medium"
        size="small"
      >
        {{ resolveStatusVariant(item.status).text }}
      </VChip>
    </template>

    <template #item.actions="{ item }">
      <div class="d-flex gap-1">
        <IconBtn
          size="small"
          @click="editItem(item)"
        >
          <VIcon icon="ri-pencil-line" />
        </IconBtn>
        <IconBtn
          size="small"
          @click="deleteItem(item)"
        >
          <VIcon icon="ri-delete-bin-line" />
        </IconBtn>
      </div>
    </template>
  </VDataTable>

      
    </VCard>


</template>

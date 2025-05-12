<script setup>
import { ref, onMounted, watch } from 'vue'
import AddNewUserDrawer from '@/components/fammeba/user/AddNewUser.vue'
import DeleteUserDialog from '@/components/fammeba/user/DeleteUserDialog.vue'
import EditUserDialog from '@/components/fammeba/user/EditUserDialog.vue'
import { $api } from '@/utils/api'

// Estados
const users = ref([])
const totalUsers = ref(0)
const searchQuery = ref('')
const selectedRole = ref()
const selectedStatus = ref()
const isAddNewUserDrawerVisible = ref(false)
const isDeleteDialogVisible = ref(false)
const isEditDialogVisible = ref(false)
const userToDelete = ref(null)
const userToEdit = ref(null)

// Data table options
const itemsPerPage = ref(10)
const page = ref(1)
const sortBy = ref()
const orderBy = ref()
const selectedRows = ref([])

// Roles y estados disponibles
const roles = ref([])
const status = [
  { title: 'Activo', value: 'ENABLED' },
  { title: 'Inactivo', value: 'DISABLED' },
]

// Headers de la tabla
const headers = [
  {
    title: 'Usuario',
    key: 'user',
    sortable: true,
  },
  {
    title: 'Email',
    key: 'email',
    sortable: true,
  },
  {
    title: 'Rol',
    key: 'role',
    sortable: true,
  },
  {
    title: 'Estado',
    key: 'status',
    sortable: true,
  },
  {
    title: 'Acciones',
    key: 'actions',
    sortable: false,
  },
]

// Funciones de utilidad
const resolveUserStatusVariant = stat => {
  const statLowerCase = stat.toLowerCase()
  if (statLowerCase === 'enabled') return 'success'
  if (statLowerCase === 'disabled') return 'secondary'
  return 'primary'
}

const resolveUserRoleVariant = role => {
  const roleLowerCase = role.toLowerCase()
  if (roleLowerCase === 'administrador') return { color: 'primary', icon: 'ri-vip-crown-line' }
  if (roleLowerCase === 'recepcionista') return { color: 'error', icon: 'ri-computer-line' }
  if (roleLowerCase === 'operador') return { color: 'info', icon: 'ri-pie-chart-line' }
  return { color: 'primary', icon: 'ri-user-line' }
}

const avatarText = name => {
  if (!name) return ''
  const nameArray = name.split(' ')
  return nameArray.map(word => word.charAt(0).toUpperCase()).join('')
}

// Función para cargar roles
const fetchRoles = async () => {
  try {
    const response = await $api('/roles', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    roles.value = response.content.map(role => ({
      title: role.name,
      value: role.name
    }))
  } catch (error) {
    console.error('Error al cargar roles:', error)
  }
}

// Computed properties
const filteredUsers = computed(() => {
  if(!searchQuery.value) return users.value;
  const query = searchQuery.value.toLowerCase();
  return users.value.filter(user => 
    (user.name?.toLowerCase() || '').includes(query) ||
    (user.lastname?.toLowerCase() || '').includes(query) ||
    (user.username?.toLowerCase() || '').includes(query) ||
    (user.email?.toLowerCase() || '').includes(query) ||
    ((user.name + ' ' + user.lastname)?.toLowerCase() || '').includes(query)
  )
})

// Función para obtener usuarios
const fetchUsers = async () => {
  try {
    const params = new URLSearchParams()
    if (selectedRole.value) params.append('role', selectedRole.value)
    if (selectedStatus.value) params.append('status', selectedStatus.value)
    if (page.value) params.append('page', page.value - 1) 
    if (itemsPerPage.value) params.append('size', itemsPerPage.value)
    if (sortBy.value) params.append('sort', `${sortBy.value},${orderBy.value}`)

    const response = await $api(`/users?${params.toString()}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    users.value = response.content || []
    totalUsers.value = response.totalElements || 0
    console.log('Respuesta de usuarios:', response)

  } catch (error) {
    console.error('Error al obtener usuarios:', error)
  }
}

// Función para actualizar opciones de la tabla
const updateOptions = options => {
  page.value = options.page
  sortBy.value = options.sortBy[0]?.key
  orderBy.value = options.sortBy[0]?.order
}

// Función para calcular la paginación
const paginationMeta = ({ page, itemsPerPage }, total) => {
  const start = (page - 1) * itemsPerPage + 1
  const end = Math.min(page * itemsPerPage, total)
  return `${start}-${end} de ${total}`
}

// Observar cambios en los filtros
watch([selectedRole, selectedStatus], () => {
  page.value = 1 
  fetchUsers()
})

// Función para abrir el diálogo de eliminación
const openDeleteDialog = (user) => {
  isDeleteDialogVisible.value = true
  userToDelete.value = user
}

// Función para manejar la eliminación del usuario
const handleUserDeleted = () => {
  isDeleteDialogVisible.value = false
  fetchUsers()
}

// Función para abrir el diálogo de edición
const openEditDialog = (user) => {
  isEditDialogVisible.value = true
  userToEdit.value = user
}

// Función para manejar la edición del usuario
const handleUserEdited = () => {
  isEditDialogVisible.value = false
  fetchUsers()
}

// Cargar datos al montar el componente
onMounted(() => {
  fetchRoles()
  fetchUsers()
})
</script>

<template>
  
  <VCard class="mb-6">
    <VCardItem class="pb-4">
        <VCardTitle>Filtros</VCardTitle>
    </VCardItem>

    <VCardText>
        <VRow>
          <!-- 👉 Select Role -->
          <VCol
            cols="12"
            sm="4"
          >
            <VSelect
              v-model="selectedRole"
              label="Seleccionar Rol"
              placeholder="Selecionar Rol"
              :items="roles"
              clearable
              clear-icon="ri-close-line"
            />
          </VCol>
          
          <!-- 👉 Select Status -->
          <VCol
            cols="12"
            sm="4"
          >
            <VSelect
              v-model="selectedStatus"
              label="Seleccionar Estado"
              placeholder="Seleccionar Estado"
              :items="status"
              clearable
              clear-icon="ri-close-line"
            />
          </VCol>
        </VRow>
      </VCardText>

      <VDivider />

      <VCardText class="d-flex flex-wrap gap-4 align-center">
        <!-- 👉 Export button -->
        <VBtn
          variant="outlined"
          color="secondary"
          prepend-icon="ri-upload-2-line"
        >
          Exportar
        </VBtn>
        <VSpacer />
        <div class="d-flex align-center gap-4 flex-wrap">
          <!-- 👉 Search  -->
          <div class="app-user-search-filter">
            <VTextField
              v-model="searchQuery"
              placeholder="Buscar Usuario"
              density="compact"
              prepend-inner-icon="ri-search-line"
            />
          </div>
          <!-- 👉 Add user button -->
          <VBtn @click="isAddNewUserDrawerVisible = true" prepend-icon="ri-user-add-fill">
            Añadir Usuario
          </VBtn>
        </div>
      </VCardText>

      <VDataTableServer
        v-model:model-value="selectedRows"
        v-model:items-per-page="itemsPerPage"
        v-model:page="page"
        :items="filteredUsers"
        item-value="id"
        :items-length="totalUsers"
        :headers="headers"
        show-select
        class="text-no-wrap rounded-0"
        @update:options="updateOptions"
      >
        <!-- User -->
        <template #item.user="{ item }">
          <div class="d-flex align-center">
            <VAvatar
              size="34"
              variant="tonal"
              :color="resolveUserRoleVariant(item.role).color"
              class="me-3"
            >
              <span>{{ avatarText(`${item.name} ${item.lastname}`) }}</span>
            </VAvatar>

            <div class="d-flex flex-column">
              <RouterLink                
                class="text-link text-base font-weight-medium"
              >
                <span class="text-base font-weight-medium">{{ `${item.name} ${item.lastname}` }}</span>
              </RouterLink>              
              <span class="text-sm text-medium-emphasis">{{ item.username }}</span>
            </div>
          </div>
        </template>

        <!-- Email -->
        <template #item.email="{ item }">
          <span> {{ item.email }}</span>
        </template>

        <!-- Role -->
        <template #item.role="{ item }">
          <div class="d-flex gap-2">
            <VIcon
              :icon="resolveUserRoleVariant(item.role).icon"
              :color="resolveUserRoleVariant(item.role).color"
              size="22"
            />
            <span class="text-capitalize text-high-emphasis">{{ item.role }}</span>
          </div>
        </template>

        <!-- Status -->
        <template #item.status="{ item }">
          <VChip
            :color="resolveUserStatusVariant(item.status)"
            size="small"
            class="text-capitalize"
          >
            {{ item.status === 'ENABLED' ? 'Activo' : 'Inactivo' }}
          </VChip>
        </template>

        <!-- Actions -->
        <template #item.actions="{ item }">
          <div class="d-flex gap-1">
            <IconBtn
              size="small" @click="openDeleteDialog(item)"          
            >
              <VIcon icon="ri-delete-bin-7-line" />
            </IconBtn>

            <IconBtn
              size="small"
              
            >
              <VIcon icon="ri-eye-line" />
            </IconBtn>

            <IconBtn
              size="small"
              color="medium-emphasis"
              @click="openEditDialog(item)"
            >
              <VIcon icon="ri-edit-box-line" />
            </IconBtn>
          </div>
        </template>

        <!-- Pagination -->
        <template #bottom>
          <VDivider />

          <div class="d-flex justify-end flex-wrap gap-x-6 px-2 py-1">
            <div class="d-flex align-center gap-x-2 text-medium-emphasis text-base">
              Rows Per Page:
              <VSelect
                v-model="itemsPerPage"
                class="per-page-select"
                variant="plain"
                :items="[10, 20, 25, 50, 100]"
              />
            </div>

            <p class="d-flex align-center text-base text-high-emphasis me-2 mb-0">
              {{ paginationMeta({ page, itemsPerPage }, totalUsers) }}
            </p>

            <div class="d-flex gap-x-2 align-center me-2">
              <VBtn
                class="flip-in-rtl"
                icon="ri-arrow-left-s-line"
                variant="text"
                density="comfortable"
                color="high-emphasis"
                :disabled="page <= 1"
                @click="page <= 1 ? page = 1 : page--"
              />

              <VBtn
                class="flip-in-rtl"
                icon="ri-arrow-right-s-line"
                density="comfortable"
                variant="text"
                color="high-emphasis"
                :disabled="page >= Math.ceil(totalUsers / itemsPerPage)"
                @click="page >= Math.ceil(totalUsers / itemsPerPage) ? page = Math.ceil(totalUsers / itemsPerPage) : page++ "
              />
            </div>
          </div>
        </template>
      </VDataTableServer>
    </VCard>

    <!-- 👉 Add New User Drawer -->
    <AddNewUserDrawer
      v-model:is-drawer-open="isAddNewUserDrawerVisible"
      @user-created="fetchUsers"
    />

    <!-- 👉 Delete User Dialog -->
    <DeleteUserDialog
      v-model:is-dialog-visible="isDeleteDialogVisible"
      :user-data="userToDelete"
      @user-deleted="handleUserDeleted"
    />

    <!-- 👉 Edit User Dialog -->
    <EditUserDialog
      v-model:is-drawer-open="isEditDialogVisible"
      :user-data="userToEdit"
      @user-created="handleUserEdited"
    />
</template>

<style lang="scss" scoped>
.app-user-search-filter {
  inline-size: 15.625rem;
}
</style>
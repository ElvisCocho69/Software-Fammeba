<script setup>
import { ref, computed } from 'vue'
import AddNewCustomer from '@/components/fammeba/customer/AddNewCustomer.vue'
import EditCustomer from '@/components/fammeba/customer/EditCustomer.vue'

// Estados
const customers = ref([])
const searchQuery = ref('')
const itemsPerPage = ref(10)
const page = ref(1)
const sortBy = ref()
const orderBy = ref()
const selectedRows = ref([])
const isAddNewCustomerDrawerVisible = ref(false)
const isEditCustomerDrawerVisible = ref(false)
const selectedCustomer = ref(null)

// Headers de la tabla
const headers = [
  {
    title: 'Nombre Completo',
    key: 'fullName',
    sortable: true,
  },
  {
    title: 'Tipo de Documento',
    key: 'documentType',
    sortable: true,
  },
  {
    title: 'Documento',
    key: 'documentNumber',
    sortable: true,
  },
  {
    title: 'Contacto',
    key: 'contact',
    sortable: true,
  },
  {
    title: 'Acciones',
    key: 'actions',
    sortable: false,
  },
]

// Datos de ejemplo para la vista
customers.value = [
  {
    id: 1,
    fullName: 'Juan Pérez',
    documentType: 'DNI',
    documentNumber: '12345678',
    contact: '+54 11 1234-5678',
    status: 'ENABLED'
  },
  {
    id: 2,
    fullName: 'María García',
    documentType: 'Pasaporte',
    documentNumber: 'AB123456',
    contact: 'maria@email.com',
    status: 'ENABLED'
  },
  {
    id: 3,
    fullName: 'Carlos López',
    documentType: 'DNI',
    documentNumber: '87654321',
    contact: '+54 11 8765-4321',
    status: 'DISABLED'
  }
]

// Computed properties
const filteredCustomers = computed(() => {
  if(!searchQuery.value) return customers.value;
  const query = searchQuery.value.toLowerCase();
  return customers.value.filter(customer => 
    customer.fullName.toLowerCase().includes(query) ||
    customer.documentNumber.toLowerCase().includes(query)
  )
})

// Función para calcular la paginación
const paginationMeta = ({ page, itemsPerPage }, total) => {
  const start = (page - 1) * itemsPerPage + 1
  const end = Math.min(page * itemsPerPage, total)
  return `${start}-${end} de ${total}`
}

const handleCustomerCreated = () => {
  // Aquí iría la lógica para recargar la lista de clientes cuando se implemente la API
  console.log('Cliente creado exitosamente')
}

const handleCustomerUpdated = () => {
  // Aquí iría la lógica para recargar la lista de clientes cuando se implemente la API
  console.log('Cliente actualizado exitosamente')
}

const openEditDrawer = (customer) => {
  selectedCustomer.value = customer
  isEditCustomerDrawerVisible.value = true
}
</script>

<template>
  <VCard class="mb-6">
    <VCardText class="d-flex flex-wrap gap-4 align-center">
      <!-- 👉 Export buttons -->
      <VBtn
        variant="tonal"
        color="error"
        prepend-icon="ri-file-pdf-2-line"
      >
        Exportar PDF
      </VBtn>

      <VBtn
        variant="tonal"
        color="#009688"
        text-color="#009688"
        prepend-icon="ri-file-excel-line"
      >
        Exportar Excel
      </VBtn>

      <VBtn
        variant="tonal"
        color="#0277BD"
        prepend-icon="ri-file-text-line"
      >
        Exportar CSV
      </VBtn>

      <VSpacer />
      <div class="d-flex align-center gap-4 flex-wrap">
        <!-- 👉 Search  -->
        <div class="app-user-search-filter">
          <VTextField
            v-model="searchQuery"
            placeholder="Buscar Cliente"
            density="compact"
            prepend-inner-icon="ri-search-line"
          />
        </div>

        <!-- 👉 Add customer button -->
        <VBtn 
          prepend-icon="ri-user-add-fill"
          @click="isAddNewCustomerDrawerVisible = true"
        >
          Añadir Cliente
        </VBtn>
      </div>
    </VCardText>

    <VDataTable
      v-model:items-per-page="itemsPerPage"
      v-model:page="page"
      :items="filteredCustomers"
      :headers="headers"
      class="text-no-wrap mt-5"
    >
      <!-- Nombre Completo -->
      <template #item.fullName="{ item }">
        <div class="d-flex align-center">
          <VAvatar
            size="34"
            variant="tonal"
            color="primary"
            class="me-3"
          >
            <span>{{ item.fullName.split(' ').map(word => word.charAt(0)).join('') }}</span>
          </VAvatar>

          <div class="d-flex flex-column">
            <span class="text-base font-weight-medium">{{ item.fullName }}</span>
          </div>
        </div>
      </template>

      <!-- Tipo de Documento -->
      <template #item.documentType="{ item }">
        <VChip
          size="small"
          color="primary"
          class="text-capitalize"
        >
          {{ item.documentType }}
        </VChip>
      </template>

      <!-- Documento -->
      <template #item.documentNumber="{ item }">
        <span>{{ item.documentNumber }}</span>
      </template>

      <!-- Contacto -->
      <template #item.contact="{ item }">
        <span>{{ item.contact }}</span>
      </template>

      <!-- Acciones -->
      <template #item.actions="{ item }">
        <div class="d-flex gap-1">
          <VTooltip location="top">
            <template #activator="{ props }">
              <IconBtn
                v-bind="props"
                size="small"
                @click="openEditDrawer(item)"
              >
                <VIcon icon="ri-pencil-line" />
              </IconBtn>
            </template>
            <span>Editar cliente</span>
          </VTooltip>

          <VTooltip location="top">
            <template #activator="{ props }">
              <IconBtn
                v-bind="props"
                size="small"
              >
                <VIcon icon="ri-forbid-2-fill" v-if="item.status === 'ENABLED'" />
                <VIcon icon="ri-checkbox-circle-line" v-else />
              </IconBtn>
            </template>
            <span>{{ item.status === 'ENABLED' ? 'Desactivar cliente' : 'Activar cliente' }}</span>
          </VTooltip>
        </div>
      </template>

      <!-- Paginación -->
      <template #bottom>
        <VDivider />

        <div class="d-flex justify-end flex-wrap gap-x-6 px-2 py-1">
          <div class="d-flex align-center gap-x-2 text-medium-emphasis text-base">
            Filas por página:
            <VSelect
              v-model="itemsPerPage"
              class="per-page-select"
              variant="plain"
              :items="[10, 20, 25, 50, 100]"
            />
          </div>

          <p class="d-flex align-center text-base text-high-emphasis me-2 mb-0">
            {{ paginationMeta({ page, itemsPerPage }, customers.length) }}
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
              :disabled="page >= Math.ceil(customers.length / itemsPerPage)"
              @click="page >= Math.ceil(customers.length / itemsPerPage) ? page = Math.ceil(customers.length / itemsPerPage) : page++"
            />
          </div>
        </div>
      </template>
    </VDataTable>
  </VCard>

  <!-- 👉 Add New Customer Drawer -->
  <AddNewCustomer
    v-model:is-drawer-open="isAddNewCustomerDrawerVisible"
    @customer-created="handleCustomerCreated"
  />

  <!-- 👉 Edit Customer Drawer -->
  <EditCustomer
    v-model:is-drawer-open="isEditCustomerDrawerVisible"
    :customer-data="selectedCustomer"
    @customer-updated="handleCustomerUpdated"
  />
</template>

<style lang="scss" scoped>
.app-user-search-filter {
  inline-size: 15.625rem;
}
</style>
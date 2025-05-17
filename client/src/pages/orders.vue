<script setup>
import { ref, computed } from 'vue'
import AddNewOrder from '@/components/fammeba/order/AddNewOrder.vue'
import EditOrder from '@/components/fammeba/order/EditOrder.vue'

// Estados
const orders = ref([])
const searchQuery = ref('')
const itemsPerPage = ref(10)
const page = ref(1)
const sortBy = ref()
const orderBy = ref()
const isAddNewOrderDrawerVisible = ref(false)
const isEditOrderDrawerVisible = ref(false)
const selectedOrder = ref(null)

// Headers de la tabla
const headers = [
  {
    title: 'Cliente',
    key: 'customer',
    sortable: true,
  },
  {
    title: 'Fecha del Pedido',
    key: 'orderDate',
    sortable: true,
  },
  {
    title: 'Fecha de Entrega Estimada',
    key: 'estimatedDeliveryDate',
    sortable: true,
  },
  {
    title: 'Estado',
    key: 'status',
    sortable: true,
  },
  {
    title: 'Total',
    key: 'total',
    sortable: true,
  },
  {
    title: 'Acciones',
    key: 'actions',
    sortable: false,
  },
]

// Datos de ejemplo para la vista
orders.value = [
  {
    id: 1,
    customer: 'Juan Pérez',
    orderDate: '2024-03-20',
    estimatedDeliveryDate: '2024-03-25',
    status: 'EN_PREPARACION',
    total: 1500.00
  },
  {
    id: 2,
    customer: 'María García',
    orderDate: '2024-03-19',
    estimatedDeliveryDate: '2024-03-24',
    status: 'ENTREGADO',
    total: 2300.50
  },
  {
    id: 3,
    customer: 'Carlos López',
    orderDate: '2024-03-18',
    estimatedDeliveryDate: '2024-03-23',
    status: 'PENDIENTE',
    total: 1800.75
  }
]

// Computed properties
const filteredOrders = computed(() => {
  if(!searchQuery.value) return orders.value;
  const query = searchQuery.value.toLowerCase();
  return orders.value.filter(order => 
    order.customer.toLowerCase().includes(query)
  )
})

// Función para calcular la paginación
const paginationMeta = ({ page, itemsPerPage }, total) => {
  const start = (page - 1) * itemsPerPage + 1
  const end = Math.min(page * itemsPerPage, total)
  return `${start}-${end} de ${total}`
}

const handleOrderCreated = () => {
  console.log('Pedido creado exitosamente')
}

const handleOrderUpdated = () => {
  console.log('Pedido actualizado exitosamente')
}

const openEditDrawer = (order) => {
  selectedOrder.value = order
  isEditOrderDrawerVisible.value = true
}

const resolveOrderStatusVariant = status => {
  const statusMap = {
    'PENDIENTE': 'warning',
    'EN_PREPARACION': 'info',
    'ENTREGADO': 'success',
    'CANCELADO': 'error'
  }
  return statusMap[status] || 'primary'
}

const formatCurrency = (value) => {
  return new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN'
  }).format(value)
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
            placeholder="Buscar Pedido"
            density="compact"
            prepend-inner-icon="ri-search-line"
          />
        </div>

        <!-- 👉 Add order button -->
        <VBtn 
          prepend-icon="ri-add-line"
          @click="isAddNewOrderDrawerVisible = true"
        >
          Añadir Pedido
        </VBtn>
      </div>
    </VCardText>

    <VDataTable
      v-model:items-per-page="itemsPerPage"
      v-model:page="page"
      :items="filteredOrders"
      :headers="headers"
      class="text-no-wrap mt-5"
    >
      <!-- Cliente -->
      <template #item.customer="{ item }">
        <div class="d-flex align-center">
          <VAvatar
            size="34"
            variant="tonal"
            color="primary"
            class="me-3"
          >
            <span>{{ item.customer.split(' ').map(word => word.charAt(0)).join('') }}</span>
          </VAvatar>

          <div class="d-flex flex-column">
            <span class="text-base font-weight-medium">{{ item.customer }}</span>
          </div>
        </div>
      </template>

      <!-- Fecha del Pedido -->
      <template #item.orderDate="{ item }">
        <span>{{ new Date(item.orderDate).toLocaleDateString() }}</span>
      </template>

      <!-- Fecha de Entrega Estimada -->
      <template #item.estimatedDeliveryDate="{ item }">
        <span>{{ new Date(item.estimatedDeliveryDate).toLocaleDateString() }}</span>
      </template>

      <!-- Estado -->
      <template #item.status="{ item }">
        <VChip
          :color="resolveOrderStatusVariant(item.status)"
          size="small"
          class="text-capitalize"
        >
          {{ item.status.replace('_', ' ') }}
        </VChip>
      </template>

      <!-- Total -->
      <template #item.total="{ item }">
        <span>{{ formatCurrency(item.total) }}</span>
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
            <span>Editar pedido</span>
          </VTooltip>

          <VTooltip location="top">
            <template #activator="{ props }">
              <IconBtn
                v-bind="props"
                size="small"
                color="error"
              >
                <VIcon icon="ri-delete-bin-line" />
              </IconBtn>
            </template>
            <span>Eliminar pedido</span>
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
            {{ paginationMeta({ page, itemsPerPage }, orders.length) }}
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
              :disabled="page >= Math.ceil(orders.length / itemsPerPage)"
              @click="page >= Math.ceil(orders.length / itemsPerPage) ? page = Math.ceil(orders.length / itemsPerPage) : page++"
            />
          </div>
        </div>
      </template>
    </VDataTable>
  </VCard>

  <!-- 👉 Add New Order Drawer -->
  <AddNewOrder
    v-model:is-drawer-open="isAddNewOrderDrawerVisible"
    @order-created="handleOrderCreated"
  />

  <!-- 👉 Edit Order Drawer -->
  <EditOrder
    v-model:is-drawer-open="isEditOrderDrawerVisible"
    :order-data="selectedOrder"
    @order-updated="handleOrderUpdated"
  />
</template>

<style lang="scss" scoped>
.app-user-search-filter {
  inline-size: 15.625rem;
}
</style>
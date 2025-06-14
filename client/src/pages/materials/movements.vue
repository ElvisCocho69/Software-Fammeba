<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { $api } from '@/utils/api'
import { isPermission } from '@/utils/constants'
import AddNewMovement from '@/components/fammeba/material/AddNewMovement.vue'

// Estados
const movements = ref([])
const totalMovements = ref(0)
const isAddNewMovementDialogVisible = ref(false)
const selectedMovementType = ref(null)
const selectedMaterialCategory = ref(null)
const selectedStartDate = ref(null)
const selectedEndDate = ref(null)
const searchTerm = ref('')
const materialCategoryOptions = ref([])

// Data table options
const itemsPerPage = ref(10)
const page = ref(1)
const sortBy = ref()
const orderBy = ref()
const selectedRows = ref([])

// Headers de la tabla
const headers = [
  {
    title: 'Fecha',
    key: 'movementdate',
    sortable: true,
  },
  {
    title: 'Material',
    key: 'material.name',
    sortable: true,
  },
  {
    title: 'Categoría',
    key: 'material.materialcategory.name'
  },
  {
    title: 'Tipo',
    key: 'movementtype',
    sortable: true,
  },
  {
    title: 'Cantidad',
    key: 'quantity',
    sortable: true,
  },
  {
    title: 'Descripción',
    key: 'description',
    sortable: true,
  },
]

// Opciones de tipo de movimiento para el filtro
const movementTypeFilterOptions = [
  { title: 'Todos', value: null },
  { title: 'Entrada', value: 'IN' },
  { title: 'Salida', value: 'OUT' },
  { title: 'Ajuste', value: 'ADJUSTMENT' },
  { title: 'Pérdida', value: 'LOSS' },
  { title: 'Devolución', value: 'RETURN' },
]

// Computed property para filtrar movimientos
const filteredMovements = computed(() => {
  if (!selectedMovementType.value) return movements.value
  return movements.value.filter(movement => 
    movement.movementtype === selectedMovementType.value
  )
})

// Función para formatear la fecha para la API
const formatDateForAPI = (date, isEndDate = false) => {
  if (!date) return null
  
  // Extraemos los componentes de la fecha directamente del string
  const [year, month, day] = date.split('-').map(Number)
  
  // Construimos la fecha con los componentes exactos
  const hours = isEndDate ? 23 : 0
  const minutes = isEndDate ? 59 : 0
  const seconds = isEndDate ? 59 : 0
  const milliseconds = isEndDate ? 999 : 0
  
  // Formateamos la fecha en formato ISO 8601 sin zona horaria para LocalDateTime
  return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}T${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}.${String(milliseconds).padStart(3, '0')}`
}

// Función para formatear la fecha para mostrar
const formatDate = (date) => {
  if (!date) return ''
  // Aseguramos que la fecha se interprete correctamente
  const d = new Date(date)
  return d.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// Función para obtener los movimientos
const fetchMovements = async () => {
  try {
    const params = new URLSearchParams()
    if (page.value) params.append('page', page.value - 1)
    if (itemsPerPage.value) params.append('size', itemsPerPage.value)
    if (sortBy.value) params.append('sort', `${sortBy.value},${orderBy.value}`)
    if (selectedMovementType.value) params.append('movementType', selectedMovementType.value)
    if (selectedMaterialCategory.value) params.append('materialCategoryName', selectedMaterialCategory.value)
    
    // Aseguramos que las fechas se envíen en el formato correcto para LocalDateTime
    if (selectedStartDate.value) {
      const startDate = formatDateForAPI(selectedStartDate.value)
      params.append('startDate', startDate)
    }
    if (selectedEndDate.value) {
      const endDate = formatDateForAPI(selectedEndDate.value, true)
      params.append('endDate', endDate)
    }
    if (searchTerm.value) params.append('searchTerm', searchTerm.value)

    const response = await $api(`/materials/movements?${params.toString()}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    movements.value = response.content || []
    totalMovements.value = response.totalElements || 0
  } catch (error) {
    console.error('Error al obtener movimientos:', error)
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

// Función para formatear el tipo de movimiento
const resolveMovementTypeVariant = type => {
  const types = {
    'IN': { color: '#4CAF50', icon: 'ri-arrow-down-line' },
    'OUT': { color: 'error', icon: 'ri-arrow-up-line' },
    'ADJUSTMENT': { color: '#FFAB00', icon: 'ri-settings-4-line' },
    'LOSS': { color: 'error', icon: 'ri-delete-bin-line' },
    'RETURN': { color: '#0288D1', icon: 'ri-reply-line' }
  }
  return types[type] || { color: 'primary', icon: 'ri-question-line' }
}

// Función para formatear el tipo de movimiento
const formatMovementType = (type) => {
  const types = {
    'IN': 'Entrada',
    'OUT': 'Salida',
    'ADJUSTMENT': 'Ajuste',
    'LOSS': 'Pérdida',
    'RETURN': 'Devolución'
  }
  return types[type] || type
}

// Función para formatear la cantidad con la unidad de medida
const formatQuantity = (material, quantity) => {
  const unit = material.measurementunit?.toLowerCase() || ''
  
  switch (unit) {
    case 'unit':
      return `${quantity} unidad${quantity !== 1 ? 'es' : ''}`
    case 'kilogram':
      return `${quantity} KG`
    case 'gram':
      return `${quantity} G`
    case 'milligram':
      return `${quantity} mG`
    case 'metre':
      return `${quantity} M`
    case 'square_metre':
      return `${quantity} M²`
    case 'cubic_metre':
      return `${quantity} M³`
    case 'centimetre':
      return `${quantity} cM`
    case 'square_centimetre':
      return `${quantity} mM²`
    case 'cubic_centimetre':
      return `${quantity} cM³`
    case 'millimetre':
      return `${quantity} mM`
    case 'square_millimetre':
      return `${quantity} mM²`
    case 'cubic_millimetre':
      return `${quantity} mM³`
    case 'liter':
      return `${quantity} L`
    case 'mililiter':
    return `${quantity} mL`
    default:
      return `${quantity}`
  }
}

// Función para obtener las categorías de materiales
const fetchMaterialCategories = async () => {
  try {
    const response = await $api('/materials/category?status=ACTIVE', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    // Agrega la opción "Todos" al inicio de la lista
    materialCategoryOptions.value = [{
      title: 'Todos',
      value: null
    }, ...response.content.map(category => ({
      title: category.name,
      value: category.name
    }))]
  } catch (error) {
    console.error('Error al obtener categorías:', error)
  }
}

// Observar cambios en los filtros
watch([selectedMovementType, selectedMaterialCategory, selectedStartDate, selectedEndDate, searchTerm], () => {
  page.value = 1 // Resetear a la primera página
  fetchMovements()
})

// Cargar datos al montar el componente
onMounted(() => {
  fetchMovements()
  fetchMaterialCategories()
})
</script>

<template>
  <VCard class="mb-6">
    <VCardItem class="pb-4">
      <VCardTitle>Filtros</VCardTitle>
    </VCardItem>

    <VCardText>
      <VRow>
        <!-- 👉 Movement Type Filter -->
        <VCol
          cols="12"
          sm="3"
        >
          <VSelect
            v-model="selectedMovementType"
            :items="movementTypeFilterOptions"
            label="Filtrar por tipo"
            prepend-inner-icon="ri-filter-3-line"
            density="compact"
            variant="outlined"
            clearable
            clear-icon="ri-close-line"
          />
        </VCol>

        <!-- 👉 Material Category Filter -->
        <VCol
          cols="12"
          sm="3"
        >
          <VSelect
            v-model="selectedMaterialCategory"
            :items="materialCategoryOptions"
            label="Filtrar por categoría"
            prepend-inner-icon="ri-folder-3-line"
            density="compact"
            variant="outlined"
            clearable
            clear-icon="ri-close-line"
          />
        </VCol>

        <!-- 👉 Start Date Filter -->
        <VCol
          cols="12"
          sm="3"
        >
          <VTextField
            v-model="selectedStartDate"
            type="date"
            label="Fecha inicio"
            density="compact"
            prepend-inner-icon="ri-calendar-line"
            clearable
            clear-icon="ri-close-line"
            :max="selectedEndDate || undefined"
            @update:model-value="fetchMovements"
          />
        </VCol>
        
        <!-- 👉 End Date Filter -->
        <VCol
          cols="12"
          sm="3"
        >
          <VTextField
            v-model="selectedEndDate"
            type="date"
            label="Fecha fin"
            density="compact"
            prepend-inner-icon="ri-calendar-line"
            clearable
            clear-icon="ri-close-line"
            :min="selectedStartDate || undefined"
            @update:model-value="fetchMovements"
          />
        </VCol>

        <!-- 👉 Search Term Filter -->
        <VCol
          cols="12"
          sm="12"
        >
          <VTextField
            v-model="searchTerm"
            label="Buscar por Nombre de Material"
            placeholder="Buscar..."
            prepend-inner-icon="ri-search-line"
            density="compact"
            variant="outlined"
            clearable
            clear-icon="ri-close-line"
          />
        </VCol>
      </VRow>
    </VCardText>

    <VDivider />

    <VCardText class="d-flex flex-wrap gap-4 align-center">
      <VSpacer />
      <div class="d-flex align-center gap-4 flex-wrap">
        <!-- 👉 Add movement button -->
        <VBtn 
          @click="isAddNewMovementDialogVisible = true" 
          prepend-icon="ri-add-line"
          v-if="isPermission('REGISTER_MOVEMENTS')"
        >
          Registrar Movimiento
        </VBtn>
      </div>
    </VCardText>

    <VDataTableServer
      v-model:model-value="selectedRows"
      v-model:items-per-page="itemsPerPage"
      v-model:page="page"
      :items="filteredMovements"
      item-value="id"
      :items-length="totalMovements"
      :headers="headers"
      show-select
      class="text-no-wrap rounded-0"
      @update:options="updateOptions"
    >
      <!-- Date -->
      <template #item.movementdate="{ item }">
        <div class="d-flex gap-2">
          <VIcon
            icon="ri-calendar-line"
            color="primary"
            size="22"
          />
          <span class="text-high-emphasis">{{ formatDate(item.movementdate) }}</span>
        </div>
      </template>

      <!-- Material -->
      <template #item.material.name="{ item }">
        <div class="d-flex gap-2">
          <VIcon
            icon="ri-box-3-line"
            color="primary"
            size="22"
          />
          <span class="text-high-emphasis">{{ item.material?.name }}</span>
        </div>
      </template>

      <!-- Material Category -->
      <template #item.material.materialcategory.name="{ item }">
        <div class="d-flex gap-2">
          <VIcon
            icon="ri-folder-line"
            color="primary"
            size="22"
          />
          <span class="text-high-emphasis">{{ item.material?.materialcategory?.name }}</span>
        </div>
      </template>

      <!-- Movement Type -->
      <template #item.movementtype="{ item }">
        <div class="d-flex gap-2">
          <VIcon
            :icon="resolveMovementTypeVariant(item.movementtype).icon"
            :color="resolveMovementTypeVariant(item.movementtype).color"
            size="22"
          />
          <VChip
            :color="resolveMovementTypeVariant(item.movementtype).color"
            size="small"
          >
            {{ formatMovementType(item.movementtype) }}
          </VChip>
        </div>
      </template>

      <!-- Quantity -->
      <template #item.quantity="{ item }">
        <div class="d-flex gap-2">
          <VIcon
            icon="ri-scales-3-line"
            color="primary"
            size="22"
          />
          <span class="text-high-emphasis">{{ formatQuantity(item.material, item.quantity) }}</span>
        </div>
      </template>

      <!-- Description -->
      <template #item.description="{ item }">
        <div class="d-flex gap-2">
          <VIcon
            icon="ri-file-text-line"
            color="primary"
            size="22"
          />
          <span class="text-high-emphasis">{{ item.description }}</span>
        </div>
      </template>

      <!-- Pagination -->
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
            {{ paginationMeta({ page, itemsPerPage }, totalMovements) }}
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
              :disabled="page >= Math.ceil(totalMovements / itemsPerPage)"
              @click="page >= Math.ceil(totalMovements / itemsPerPage) ? page = Math.ceil(totalMovements / itemsPerPage) : page++ "
            />
          </div>
        </div>
      </template>
    </VDataTableServer>
  </VCard>

  <!-- 👉 Add New Movement Dialog -->
  <AddNewMovement
    v-model:is-dialog-visible="isAddNewMovementDialogVisible"
    @movement-registered="fetchMovements"
  />
</template>
<script setup>
import { ref, onMounted, computed } from 'vue'
import { $api } from '@/utils/api'
import { Chart, registerables } from 'chart.js'
import { Line, Bar, Doughnut } from 'vue-chartjs'

// Registrar componentes de Chart.js
Chart.register(...registerables)

// Estados
const materials = ref([])
const movements = ref([])
const customers = ref([])
const materialsInventory = ref({})
const loading = ref(true)

// KPIs calculados
const totalMaterials = computed(() => materials.value.length)
const totalCustomers = computed(() => customers.value.length)
const totalMovements = computed(() => movements.value.length)

// Cálculo de movimientos por tipo
const movimientosPorTipo = computed(() => {
  return movements.value.reduce((acc, movement) => {
    acc[movement.movementtype] = (acc[movement.movementtype] || 0) + 1
    return acc
  }, {})
})

// Cálculo de materiales más movidos
const materialesMasMovidos = computed(() => {
  const movimientosPorMaterial = movements.value.reduce((acc, movement) => {
    const materialCode = movement.material?.code
    if (materialCode) {
      acc[materialCode] = (acc[materialCode] || 0) + 1
    }
    return acc
  }, {})

  return Object.entries(movimientosPorMaterial)
    .map(([code, count]) => {
      const material = materials.value.find(m => m.code === code)
      return {
        code,
        name: material?.name || 'Material Desconocido',
        count
      }
    })
    .sort((a, b) => b.count - a.count)
    .slice(0, 5) // Top 5 materiales más movidos
})

// Cálculo de stock por categoría
const stockPorCategoria = computed(() => {
  return materials.value.reduce((acc, material) => {
    const categoria = material.materialcategory?.name || 'Sin categoría'
    acc[categoria] = (acc[categoria] || 0) + (materialsInventory.value[material.code] || 0)
    return acc
  }, {})
})

// Cálculo de clientes por tipo
const clientesPorTipo = computed(() => {
  return customers.value.reduce((acc, customer) => {
    acc[customer.clientType] = (acc[customer.clientType] || 0) + 1
    return acc
  }, {})
})

// Datos para gráficos
const movimientosChartData = computed(() => ({
  labels: Object.keys(movimientosPorTipo.value).map(tipo => {
    const tipos = {
      'IN': 'Entrada',
      'OUT': 'Salida',
      'ADJUSTMENT': 'Ajuste',
      'LOSS': 'Pérdida',
      'RETURN': 'Devolución'
    }
    return tipos[tipo] || tipo
  }),
  datasets: [{
    label: 'Movimientos por Tipo',
    data: Object.values(movimientosPorTipo.value),
    backgroundColor: [
      'rgba(76, 175, 80, 0.8)',  // Entrada
      'rgba(244, 67, 54, 0.8)',  // Salida
      'rgba(255, 193, 7, 0.8)',  // Ajuste
      'rgba(156, 39, 176, 0.8)', // Pérdida
      'rgba(33, 150, 243, 0.8)'  // Devolución
    ],
    borderColor: [
      'rgb(76, 175, 80)',
      'rgb(244, 67, 54)',
      'rgb(255, 193, 7)',
      'rgb(156, 39, 176)',
      'rgb(33, 150, 243)'
    ],
    borderWidth: 1
  }]
}))

const stockChartData = computed(() => ({
  labels: Object.keys(stockPorCategoria.value),
  datasets: [{
    label: 'Stock por Categoría',
    data: Object.values(stockPorCategoria.value),
    backgroundColor: [
      'rgba(76, 175, 80, 0.8)',   // Verde
      'rgba(33, 150, 243, 0.8)',  // Azul
      'rgba(156, 39, 176, 0.8)',  // Púrpura
      'rgba(255, 193, 7, 0.8)',   // Amarillo
      'rgba(244, 67, 54, 0.8)',   // Rojo
      'rgba(0, 188, 212, 0.8)',   // Cyan
      'rgba(255, 87, 34, 0.8)',   // Naranja
      'rgba(121, 85, 72, 0.8)'    // Marrón
    ],
    borderColor: [
      'rgb(76, 175, 80)',
      'rgb(33, 150, 243)',
      'rgb(156, 39, 176)',
      'rgb(255, 193, 7)',
      'rgb(244, 67, 54)',
      'rgb(0, 188, 212)',
      'rgb(255, 87, 34)',
      'rgb(121, 85, 72)'
    ],
    borderWidth: 1
  }]
}))

const clientesChartData = computed(() => ({
  labels: Object.keys(clientesPorTipo.value).map(tipo => 
    tipo === 'NATURAL' ? 'Natural' : 'Jurídico'
  ),
  datasets: [{
    label: 'Clientes por Tipo',
    data: Object.values(clientesPorTipo.value),
    backgroundColor: [
      'rgba(76, 175, 80, 0.8)',
      'rgba(33, 150, 243, 0.8)'
    ],
    borderColor: [
      'rgb(76, 175, 80)',
      'rgb(33, 150, 243)'
    ],
    borderWidth: 1
  }]
}))

const materialesMasMovidosChartData = computed(() => ({
  labels: materialesMasMovidos.value.map(m => m.name),
  datasets: [{
    label: 'Movimientos',
    data: materialesMasMovidos.value.map(m => m.count),
    backgroundColor: 'rgba(156, 39, 176, 0.8)',
    borderColor: 'rgb(156, 39, 176)',
    borderWidth: 1
  }]
}))

// Opciones de gráficos
const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom',
      labels: {
        padding: 20,
        font: {
          size: 12
        }
      }
    }
  },
  elements: {
    arc: {
      borderWidth: 0
    }
  }
}

const barChartOptions = {
  ...chartOptions,
  scales: {
    y: {
      beginAtZero: true,
      grid: {
        display: false
      }
    },
    x: {
      grid: {
        display: false
      }
    }
  }
}

// Cargar datos
const fetchData = async () => {
  try {
    loading.value = true
    // Cargar materiales
    const materialsResponse = await $api('/materials?size=1000', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    materials.value = materialsResponse.content || []

    // Cargar movimientos
    const movementsResponse = await $api('/materials/movements?size=1000', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    movements.value = movementsResponse.content || []

    // Cargar clientes
    const customersResponse = await $api('/clients?size=1000', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    customers.value = customersResponse.content || []

    // Cargar inventario para cada material
    for (const material of materials.value) {
      const inventory = await $api(`/materials/inventory/${material.code}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      materialsInventory.value[material.code] = inventory.quantity || 0
    }
  } catch (error) {
    console.error('Error al cargar datos:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <VContainer class="py-6">
    <VRow>
      <!-- Tarjetas de Resumen -->
      <VCol cols="12" sm="6" md="4">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText class="text-center">
            <VIcon
              icon="ri-box-3-line"
              color="primary"
              size="40"
              class="mb-2"
            />
            <div class="text-h4 font-weight-bold mb-1">{{ totalMaterials }}</div>
            <div class="text-subtitle-1 text-medium-emphasis">Materiales</div>
          </VCardText>
        </VCard>
      </VCol>

      <VCol cols="12" sm="6" md="4">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText class="text-center">
            <VIcon
              icon="ri-user-line"
              color="primary"
              size="40"
              class="mb-2"
            />
            <div class="text-h4 font-weight-bold mb-1">{{ totalCustomers }}</div>
            <div class="text-subtitle-1 text-medium-emphasis">Clientes</div>
          </VCardText>
        </VCard>
      </VCol>

      <VCol cols="12" sm="6" md="4">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText class="text-center">
            <VIcon
              icon="ri-exchange-line"
              color="primary"
              size="40"
              class="mb-2"
            />
            <div class="text-h4 font-weight-bold mb-1">{{ totalMovements }}</div>
            <div class="text-subtitle-1 text-medium-emphasis">Movimientos</div>
          </VCardText>
        </VCard>
      </VCol>

      <!-- Gráficos -->
      <VCol cols="12" md="6">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText>
            <div class="text-h6 mb-4 d-flex align-center">
              <VIcon
                icon="ri-exchange-line"
                color="primary"
                class="me-2"
              />
              Movimientos por Tipo
            </div>
            <div style="height: 300px">
              <Doughnut
                :data="movimientosChartData"
                :options="chartOptions"
              />
            </div>
          </VCardText>
        </VCard>
      </VCol>

      <VCol cols="12" md="6">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText>
            <div class="text-h6 mb-4 d-flex align-center">
              <VIcon
                icon="ri-bar-chart-box-line"
                color="primary"
                class="me-2"
              />
              Materiales más Movidos
            </div>
            <div style="height: 300px">
              <Bar
                :data="materialesMasMovidosChartData"
                :options="barChartOptions"
              />
            </div>
          </VCardText>
        </VCard>
      </VCol>

      <VCol cols="12" md="6">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText>
            <div class="text-h6 mb-4 d-flex align-center">
              <VIcon
                icon="ri-stack-line"
                color="primary"
                class="me-2"
              />
              Stock por Categoría
            </div>
            <div style="height: 300px">
              <Doughnut
                :data="stockChartData"
                :options="chartOptions"
              />
            </div>
          </VCardText>
        </VCard>
      </VCol>

      <VCol cols="12" md="6">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText>
            <div class="text-h6 mb-4 d-flex align-center">
              <VIcon
                icon="ri-user-line"
                color="primary"
                class="me-2"
              />
              Distribución de Clientes
            </div>
            <div style="height: 300px">
              <Doughnut
                :data="clientesChartData"
                :options="chartOptions"
              />
            </div>
          </VCardText>
        </VCard>
      </VCol>

      <!-- Tabla de Materiales con Stock Bajo -->
      <VCol cols="12">
        <VCard
          class="dashboard-card"
          elevation="2"
        >
          <VCardText>
            <div class="text-h6 mb-4 d-flex align-center">
              <VIcon
                icon="ri-alert-line"
                color="error"
                class="me-2"
              />
              Materiales con Stock Bajo
            </div>
            <VTable class="stock-table">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nombre</th>
                  <th>Categoría</th>
                  <th>Stock Actual</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="material in materials.filter(m => (materialsInventory[m.code] || 0) < 10)"
                  :key="material.code"
                >
                  <td>{{ material.code }}</td>
                  <td>{{ material.name }}</td>
                  <td>{{ material.materialcategory?.name }}</td>
                  <td>
                    <VChip
                      color="error"
                      size="small"
                    >
                      {{ materialsInventory[material.code] || 0 }}
                    </VChip>
                  </td>
                </tr>
              </tbody>
            </VTable>
          </VCardText>
        </VCard>
      </VCol>
    </VRow>
  </VContainer>
</template>

<style scoped>
.dashboard-card {
  transition: all 0.3s ease;
  border-radius: 12px;
  overflow: hidden;
}

.dashboard-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1) !important;
}

.stock-table {
  border-radius: 8px;
  overflow: hidden;
}

.stock-table th {
  background-color: rgb(var(--v-theme-surface));
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.875rem;
  letter-spacing: 0.5px;
}

.stock-table td {
  padding: 16px;
}

.v-card-text {
  padding: 24px;
}
</style>
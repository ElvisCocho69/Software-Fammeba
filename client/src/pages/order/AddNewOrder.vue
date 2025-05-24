<script setup>
import { ref, onMounted } from 'vue'
import InvoiceEditable from '@/views/order/InvoiceEditable.vue'
import InvoiceSendInvoiceDrawer from '@/views/order/InvoiceSendInvoiceDrawer.vue'
import { $api } from '@/utils/api'
import { useRouter } from 'vue-router'

// Estados para clientes
const clients = ref([])
const selectedClientId = ref(null)
const selectedClient = ref(null)
const loading = ref(false)
const error = ref(null)
const success = ref(null)

// Función para obtener clientes
const fetchClients = async () => {
  try {
    loading.value = true
    const response = await $api('/clients', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    clients.value = response.content.map(client => ({
      ...client,
      title: client.clientType === 'NATURAL' 
        ? `${client.name} ${client.lastname}`
        : client.razonsocial,
      value: client.id
    }))
  } catch (error) {
    console.error('Error al obtener clientes:', error)
  } finally {
    loading.value = false
  }
}

// Función para manejar la selección de cliente
const handleClientChange = (clientId) => {
  selectedClientId.value = clientId
  const client = clients.value.find(c => c.value === clientId)
  if (client) {
    selectedClient.value = client
  }
}

// Cargar clientes al montar el componente
onMounted(() => {
  fetchClients()
})

// Estructura de datos actualizada
const orderData = ref({
  ordernumber: '', // Se generará automáticamente
  orderdate: new Date(),
  deliverydate: null,
  description: '',
  specialnotes: '',
  status: 'PENDIENTE',
  totalprice: 0,
  clientId: null,
  userId: null, // Se obtendrá del usuario autenticado
  orderDetails: [{
    quantity: 1,
    unitprice: 0,
    status: 'PENDIENTE',
    structure: {
      name: '',
      description: '',
      colors: '',
      materials: '',
      startdate: null,
      estimatedenddate: null,
      observations: ''
    }
  }]
})

const isSendPaymentSidebarVisible = ref(false)

const addProduct = value => {
  orderData.value.orderDetails.push(value)
}

const removeProduct = id => {
  orderData.value.orderDetails.splice(id, 1)
}

const updateProduct = ({ id, data }) => {
  orderData.value.orderDetails[id] = data
}

// Función para guardar la orden
const saveOrder = async () => {
  try {
    loading.value = true
    error.value = null
    success.value = null

    // Validaciones básicas
    if (!orderData.value.clientId) {
      throw new Error('Debe seleccionar un cliente')
    }
    if (!orderData.value.orderdate) {
      throw new Error('Debe especificar la fecha del pedido')
    }
    if (!orderData.value.deliverydate) {
      throw new Error('Debe especificar la fecha de entrega')
    }
    if (orderData.value.orderDetails.length === 0) {
      throw new Error('Debe agregar al menos un detalle a la orden')
    }

    // Calcular el precio total
    orderData.value.totalprice = orderData.value.orderDetails.reduce((total, detail) => {
      return total + (detail.quantity * detail.unitprice)
    }, 0)

    // Obtener el ID del usuario del token
    const token = localStorage.getItem('token')
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]))
      orderData.value.userId = payload.id
    }

    // Enviar la orden al backend
    const response = await $api('/orders', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(orderData.value)
    })

    success.value = 'Orden guardada exitosamente'
    
    // Redirigir a la lista de órdenes después de 2 segundos
    setTimeout(() => {
      router.push({ name: 'orders' })
    }, 2000)

  } catch (err) {
    error.value = err.message || 'Error al guardar la orden'
  } finally {
    loading.value = false
  }
}

const router = useRouter()

const goToCustomers = () => {
  router.push({ name: 'customers' })
}
</script>

<template>
  <VRow>
    <!-- 👉 OrderEditable -->
    <VCol cols="12" md="9">
      <InvoiceEditable
        :data="orderData"
        :clients="clients"
        :selected-client-id="selectedClientId"
        @update-client="handleClientChange"
        @push="addProduct"
        @remove="removeProduct"
        @update-product="updateProduct"
      />

      <!-- 👉 Alerts -->
      <VAlert
        v-if="error"
        type="error"
        closable
        class="mt-4"
      >
        {{ error }}
      </VAlert>

      <VAlert
        v-if="success"
        type="success"
        closable
        class="mt-4"
      >
        {{ success }}
      </VAlert>
    </VCol>

    <!-- 👉 Right Column: Order Actions -->
    <VCol
      cols="12"
      md="3"
    >
      <VCard class="mb-6">
        <VCardText>
          <!-- 👉 Save -->
          <VBtn
            block
            color="primary"
            class="mb-4"
            :loading="loading"
            @click="saveOrder"
          >
            Guardar Orden
          </VBtn>

          <!-- 👉 Preview -->
          <VBtn
            block
            color="secondary"
            variant="outlined"
            class="mb-4"
          >
            Vista Previa
          </VBtn>

          <!-- 👉 Send Invoice -->
          <VBtn
            block
            color="secondary"
            variant="outlined"
            @click="isSendPaymentSidebarVisible = true"
          >
            Enviar Comprobante
          </VBtn>
        </VCardText>
      </VCard>
    </VCol>
  </VRow>

  <!-- 👉 Send Invoice Sidebar -->
  <InvoiceSendInvoiceDrawer v-model:isDrawerOpen="isSendPaymentSidebarVisible" />
</template>

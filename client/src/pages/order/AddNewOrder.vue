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
    // Actualizar los datos del cliente en invoiceData
    invoiceData.value.invoice.client = {
      name: client.clientType === 'NATURAL' ? `${client.name} ${client.lastname}` : client.razonsocial,
      documentNumber: client.documentnumber,
      address: client.address,
      contact: client.contact,
      email: client.email,
      company: client.clientType === 'JURIDICO' ? client.razonsocial : '',
      companyEmail: client.email,
      country: 'Perú'
    }
  }
}

// Cargar clientes al montar el componente
onMounted(() => {
  fetchClients()
})

// 👉 Default Blank Data
const invoiceData = ref({
  invoice: {
    id: 1,
    issuedDate: '',
    service: '',
    total: 0,
    avatar: '',
    invoiceStatus: '',
    dueDate: '',
    balance: 0,
    client: {
      address: '112, Lorem Ipsum, Florida',
      company: 'Greeva Inc',
      companyEmail: 'johndoe@greeva.com',
      contact: '+1 123 3452 12',
      country: 'USA',
      name: 'John Doe',
    },
  },
  paymentDetails: {
    totalDue: '$12,110.55',
    bankName: 'American Bank',
    country: 'United States',
    iban: 'ETD95476213',
    swiftCode: 'BR91905',
  },
  purchasedProducts: [{
    title: '',
    cost: 0,
    hours: 0,
    description: '',
  }],
  note: '',
  paymentMethod: '',
  salesperson: '',
  thanksNote: '',
})

const paymentMethods = [
  'Efectivo',
  'Transferencia',
  'Yape',
]

const isSendPaymentSidebarVisible = ref(false)

const addProduct = value => {
  invoiceData.value?.purchasedProducts.push(value)
}

const removeProduct = id => {
  invoiceData.value?.purchasedProducts.splice(id, 1)
}

const router = useRouter()

const goToCustomers = () => {
  router.push({ name: 'customers' })
}
</script>

<template>
  <VRow>
    <!-- 👉 InvoiceEditable -->
    <VCol cols="12" md="9">
      <InvoiceEditable
        :data="invoiceData"
        :clients="clients"
        :selected-client-id="selectedClientId"
        @update-client="handleClientChange"
        @push="addProduct"
        @remove="removeProduct"
      />
    </VCol>

    <!-- 👉 Right Column: Invoice Action -->
    <VCol
      cols="12"
      md="3"
    >
      <VCard class="mb-6">
        <VCardText>
          <!-- 👉 Send Invoice -->
          <VBtn
            block
            prepend-icon="ri-send-plane-line"
            class="mb-4"
            @click="isSendPaymentSidebarVisible = true"
          >
            Enviar Comprobante
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

          <!-- 👉 Save -->
          <VBtn
            block
            color="secondary"
            variant="outlined"
          >
            Guardar
          </VBtn>
        </VCardText>
      </VCard>

      <!-- 👉 Select payment method -->
      <VSelect
        :items="paymentMethods"
        label="Método de Pago"
        class="mb-4"
      />

      
    </VCol>
  </VRow>

  <!-- 👉 Send Invoice Sidebar -->
  <InvoiceSendInvoiceDrawer v-model:isDrawerOpen="isSendPaymentSidebarVisible" />
</template>

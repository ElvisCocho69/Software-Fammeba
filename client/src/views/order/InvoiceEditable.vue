<script setup>
import InvoiceProductEdit from './InvoiceProductEdit.vue'
import { VNodeRenderer } from '@layouts/components/VNodeRenderer'
import { themeConfig } from '@themeConfig'
import { useRouter } from 'vue-router'

const props = defineProps({
  data: {
    type: null,
    required: true,
  },
  clients: {
    type: Array,
    required: false,
    default: () => [],
  },
  selectedClientId: {
    type: [String, Number, null],
    required: false,
    default: null,
  },
})

const emit = defineEmits([
  'push',
  'remove',
  'update-client',
])

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

const router = useRouter()

const handleClientSelect = (clientId) => {
  emit('update-client', clientId)
  orderData.value.clientId = clientId
}

// 👉 Add item function
const addItem = () => {
  orderData.value.orderDetails.push({
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
  })
}

const removeProduct = id => {
  orderData.value.orderDetails.splice(id, 1)
}

const internalSelectedClientId = computed({
  get: () => props.selectedClientId,
  set: (val) => emit('update-client', val),
})

const selectedClient = computed(() => props.clients.find(c => c.value === props.selectedClientId))

const goToCustomers = () => {
  router.push({ name: 'customers' })
}

// Calcular el total
const calculateTotal = computed(() => {
  return orderData.value.orderDetails.reduce((total, detail) => {
    return total + (detail.quantity * detail.unitprice)
  }, 0)
})
</script>

<template>
  <VCard class="pa-sm-12 pa-6">
    <!-- SECTION Header -->
    <div class="d-flex flex-wrap justify-space-between flex-column rounded bg-var-theme-background flex-sm-row gap-6 pa-6 mb-6">
      <!-- 👉 Left Content -->
      <div>
        <div class="app-logo mb-6">
          <VNodeRenderer :nodes="themeConfig.app.logo" />
          <h1 class="app-logo-title">
            {{ themeConfig.app.title }}
          </h1>
        </div>

        <!-- 👉 Address -->
        <p class="text-high-emphasis mb-0 text-body-1">
          Oficina 237, Av. América Sur 1234, Urb. La Merced
        </p>
        <p class="text-high-emphasis mb-0 text-body-1">
          Trujillo, La Libertad 13007, Perú
        </p>
        <p class="text-high-emphasis mb-0 text-body-1">
          +51 44 123 4567, +51 944 987 654
        </p>
      </div>

      <!-- 👉 Right Content -->
      <div class="d-flex gap-2 flex-column">
        <!-- 👉 Order Number -->
        <div class="d-flex align-start align-sm-center font-weight-medium justify-sm-end flex-column flex-sm-row text-lg">
          <span class="text-high-emphasis me-4" style="inline-size: 5.625rem;">Pedido</span>
          <span>
            <VTextField
              v-model="orderData.ordernumber"
              disabled
              density="compact"
              prefix="#"
              style="inline-size: 9.5rem;"
            />
          </span>
        </div>

        <!-- 👉 Order Date -->
        <div class="d-flex align-start align-sm-center justify-sm-end flex-column flex-sm-row">
          <span class="text-high-emphasis me-4" style="inline-size: 7rem;">Fecha Pedido:</span>
          <span style="inline-size: 9.5rem;">
            <AppDateTimePicker
              v-model="orderData.orderdate"
              density="compact"
              placeholder="YYYY-MM-DD"
              :config="{ position: 'auto right' }"
            />
          </span>
        </div>

        <!-- 👉 Delivery Date -->
        <div class="d-flex align-start align-sm-center justify-sm-end flex-column flex-sm-row">
          <span class="text-high-emphasis me-4" style="inline-size: 7rem;">Fecha Entrega:</span>
          <span style="min-inline-size: 9.5rem;">
            <AppDateTimePicker
              v-model="orderData.deliverydate"
              density="compact"
              placeholder="YYYY-MM-DD"
              :config="{ position: 'auto right' }"
            />
          </span>
        </div>
      </div>
    </div>

    <!-- 👉 Cliente Section -->
    <div style="max-width: 400px;">
      <VCol class="text-no-wrap" style="padding-left: 0; padding-right: 0;">
        <h6 class="text-h6 mb-4">Cliente:</h6>
        <VRow class="mb-4" align="center">
          <VCol cols="8">
            <VSelect
              v-model="internalSelectedClientId"
              :items="props.clients"
              item-title="title"
              item-value="value"
              placeholder="Seleccionar Cliente"
              class="mb-0"
              style="inline-size: 100%;"
              @update:model-value="handleClientSelect"
            />
          </VCol>
          <VCol cols="4" class="d-flex align-center">
            <VBtn 
              prepend-icon="ri-user-add-fill"
              @click="goToCustomers"
              style="min-width: 150px;"
            >
              Añadir Cliente
            </VBtn>
          </VCol>
        </VRow>
        <template v-if="selectedClient">
          <p class="mb-0 text-body-1" v-if="selectedClient.clientType === 'NATURAL'">
            {{ selectedClient.name }} {{ selectedClient.lastname }}
          </p>
          <p class="mb-0 text-body-1" v-if="selectedClient.clientType === 'JURIDICO'">
            {{ selectedClient.razonsocial }}
          </p>
          <p class="mb-0 text-body-1">{{ selectedClient.address }}</p>
          <p class="mb-0 text-body-1" v-if="selectedClient.clientType === 'NATURAL'">
            DNI: {{ selectedClient.documentnumber }}
          </p>
          <p class="mb-0 text-body-1" v-if="selectedClient.clientType === 'JURIDICO'">
            RUC: {{ selectedClient.documentnumber }}
          </p>
          <p class="mb-0 text-body-1">{{ selectedClient.contact }}</p>
          <p class="mb-0 text-body-1">{{ selectedClient.email }}</p>
        </template>
      </VCol>
    </div>

    <VDivider class="my-6 border-dashed" />

    <!-- 👉 Description and Special Notes -->
    <VRow>
      <VCol cols="12" md="6">
        <VTextarea
          v-model="orderData.description"
          label="Descripción"
          placeholder="Descripción detallada del pedido"
          rows="3"
        />
      </VCol>
      <VCol cols="12" md="6">
        <VTextarea
          v-model="orderData.specialnotes"
          label="Notas Especiales"
          placeholder="Notas o instrucciones especiales"
          rows="3"
        />
      </VCol>
    </VRow>

    <VDivider class="my-6 border-dashed" />

    <!-- 👉 Add purchased products -->
    <div class="add-products-form">
      <div
        v-for="(product, index) in orderData.orderDetails"
        :key="index"
        class="mb-4"
      >
        <InvoiceProductEdit
          :id="index"
          :data="product"
          @remove-product="removeProduct"
          @update-product="updateProduct"
        />
      </div>

      <VBtn
        size="small"
        prepend-icon="ri-add-line"
        @click="addItem"
        class="mt-4"
      >
        Añadir Estructura
      </VBtn>
    </div>

    <VDivider class="my-6 border-dashed" />

    <!-- 👉 Total Amount -->
    <div class="d-flex justify-space-between flex-wrap flex-column flex-sm-row">
      <div class="mb-6 mb-sm-0">
        <h6 class="text-h6 mb-4">Estado del Pedido:</h6>
        <VSelect
          v-model="orderData.status"
          :items="[
            { title: 'Pendiente', value: 'PENDIENTE' },
            { title: 'En Preparación', value: 'EN_PREPARACION' },
            { title: 'Entregado', value: 'ENTREGADO' },
            { title: 'Cancelado', value: 'CANCELADO' }
          ]"
          item-title="title"
          item-value="value"
          label="Estado"
          class="mb-4"
        />
      </div>

      <div>
        <table class="w-100">
          <tbody>
            <tr>
              <td class="pe-16 text-body-1">Subtotal:</td>
              <td :class="$vuetify.locale.isRtl ? 'text-start' : 'text-end'">
                <h6 class="text-h6">S/. {{ calculateTotal.toFixed(2) }}</h6>
              </td>
            </tr>
            <tr>
              <td class="pe-16 text-body-1">IGV (18%):</td>
              <td :class="$vuetify.locale.isRtl ? 'text-start' : 'text-end'">
                <h6 class="text-h6">S/. {{ (calculateTotal * 0.18).toFixed(2) }}</h6>
              </td>
            </tr>
          </tbody>
        </table>

        <VDivider class="my-2" />

        <table class="w-100">
          <tbody>
            <tr>
              <td class="pe-16 text-body-1">Total:</td>
              <td :class="$vuetify.locale.isRtl ? 'text-start' : 'text-end'">
                <h6 class="text-h6">S/. {{ (calculateTotal * 1.18).toFixed(2) }}</h6>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </VCard>
</template>

<style scoped>
.app-logo {
  display: flex;
  align-items: center;
  column-gap: 0.5rem;

  .app-logo-title {
    font-size: 1.25rem;
    font-weight: 600;
    letter-spacing: 0.27px;
    line-height: 1.5rem;
    text-transform: capitalize;
  }
}
</style>

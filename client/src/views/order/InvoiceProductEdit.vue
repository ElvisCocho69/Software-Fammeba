<!-- eslint-disable vue/no-mutating-props -->
<script setup>
const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
  data: {
    type: Object,
    required: true,
    default: () => ({
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
    }),
  },
})

const emit = defineEmits([
  'removeProduct',
  'updateProduct',
])

const localProductData = ref(structuredClone(toRaw(props.data)))

const removeProduct = () => {
  emit('removeProduct', props.id)
}

const updateProduct = () => {
  emit('updateProduct', {
    id: props.id,
    data: localProductData.value
  })
}

// Observar cambios en los datos locales
watch(localProductData, () => {
  updateProduct()
}, { deep: true })

const orderDetailStatus = [
  { title: 'Pendiente', value: 'PENDIENTE' },
  { title: 'En Preparación', value: 'EN_PREPARACION' },
  { title: 'Completado', value: 'COMPLETADO' },
  { title: 'Cancelado', value: 'CANCELADO' }
]
</script>

<template>
  <div class="add-products-header mb-2 d-none d-md-flex mb-4">
    <VRow class="me-10">
      <VCol cols="12">
        <h6 class="text-h6">
          Estructura
        </h6>
      </VCol>
    </VRow>
  </div>

  <VCard
    flat
    border
    class="d-flex flex-sm-row flex-column-reverse"
  >
    <!-- 👉 Left Form -->
    <div class="pa-5 flex-grow-1">
      <!-- Detalles de la Orden -->
      <VRow class="mb-2">
        <VCol cols="12" md="4">
          <VTextField
            v-model.number="localProductData.quantity"
            type="number"
            label="Cantidad"
            min="1"
            class="mb-2"
          />
        </VCol>
        <VCol cols="12" md="4">
          <VTextField
            v-model.number="localProductData.unitprice"
            type="number"
            label="Precio Unitario"
            min="0"
            prefix="S/."
            class="mb-2"
          />
        </VCol>
        <VCol cols="12" md="4">
          <VSelect
            v-model="localProductData.status"
            :items="orderDetailStatus"
            item-title="title"
            item-value="value"
            label="Estado"
            class="mb-2"
          />
        </VCol>
      </VRow>

      <!-- Detalles de la Estructura -->
      <VRow class="mb-2">
        <VCol cols="12" md="6">
          <VTextField
            v-model="localProductData.structure.name"
            label="Nombre de la Estructura"
            placeholder="Ej: Columna Principal"
            class="mb-2"
          />
        </VCol>
        <VCol cols="12" md="6">
          <VTextField
            v-model="localProductData.structure.description"
            label="Descripción"
            placeholder="Descripción de la estructura"
            class="mb-2"
          />
        </VCol>
      </VRow>

      <VRow class="mb-2">
        <VCol cols="12" md="6">
          <VTextField
            v-model="localProductData.structure.colors"
            label="Colores"
            placeholder="Ej: Rojo, Azul"
            class="mb-2"
          />
        </VCol>
        <VCol cols="12" md="6">
          <VTextField
            v-model="localProductData.structure.materials"
            label="Materiales"
            placeholder="Ej: Madera, Metal"
            class="mb-2"
          />
        </VCol>
      </VRow>

      <VRow class="mb-2">
        <VCol cols="12" md="6">
          <AppDateTimePicker
            v-model="localProductData.structure.startdate"
            label="Fecha de Inicio"
            placeholder="YYYY-MM-DD"
            class="mb-2"
          />
        </VCol>
        <VCol cols="12" md="6">
          <AppDateTimePicker
            v-model="localProductData.structure.estimatedenddate"
            label="Fecha Estimada de Finalización"
            placeholder="YYYY-MM-DD"
            class="mb-2"
          />
        </VCol>
      </VRow>

      <VRow>
        <VCol cols="12">
          <VTextarea
            v-model="localProductData.structure.observations"
            rows="3"
            label="Observaciones"
            placeholder="Observaciones adicionales sobre la estructura"
            class="mb-2"
          />
        </VCol>
      </VRow>
    </div>

    <!-- 👉 Item Actions -->
    <div
      class="d-flex flex-column align-end item-actions"
      :class="$vuetify.display.smAndUp ? 'border-s' : 'border-b' "
    >
      <IconBtn @click="removeProduct">
        <VIcon
          :size="24"
          icon="ri-close-line"
        />
      </IconBtn>
    </div>
  </VCard>
</template>

<style scoped>
.add-products-header {
  position: relative;
}
</style>

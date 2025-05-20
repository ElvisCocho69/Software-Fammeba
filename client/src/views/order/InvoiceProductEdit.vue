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
      nombre: '',
      tipo: '',
      dimensiones: '',
      pesoEstimado: 0,
      descripcionTecnica: '',
    }),
  },
})

const emit = defineEmits([
  'removeProduct',
  'totalAmount',
])

const tiposEstructura = [
  'Columna',
  'Viga',
  'Losa',
  'Muro',
  'Fundación',
  'Otro',
]

const localProductData = ref(structuredClone(toRaw(props.data)))

const removeProduct = () => {
  emit('removeProduct', props.id)
}

// Calculamos el total basado en el peso estimado (esto es un ejemplo, ajusta según tu lógica de negocio)
const totalPrice = computed(() => Number(localProductData.value.pesoEstimado) * 100) // Asumiendo $100 por unidad de peso

watch(totalPrice, () => {
  emit('totalAmount', totalPrice.value)
}, { immediate: true })
</script>

<template>
  <!-- eslint-disable vue/no-mutating-props -->
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
      <VRow class="mb-2">
        <VCol cols="12" md="6">
          <VTextField
            v-model="localProductData.nombre"
            label="Nombre de la Estructura"
            placeholder="Ej: Columna Principal"
            class="mb-2"
          />
        </VCol>
        <VCol cols="12" md="6">
          <VSelect
            v-model="localProductData.tipo"
            :items="tiposEstructura"
            label="Tipo de Estructura"
            placeholder="Seleccionar tipo"
            class="mb-2"
          />
        </VCol>
      </VRow>
      <VRow class="mb-2">
        <VCol cols="12" md="6">
          <VTextField
            v-model="localProductData.dimensiones"
            label="Dimensiones"
            placeholder="Ej: 30x40x300 cm"
            class="mb-2"
          />
        </VCol>
        <VCol cols="12" md="6">
          <VTextField
            v-model="localProductData.pesoEstimado"
            type="number"
            label="Peso Estimado"
            placeholder="0"
            suffix="kg"
            class="mb-2"
          />
        </VCol>
      </VRow>
      <VRow>
        <VCol cols="12">
          <VTextarea
            v-model="localProductData.descripcionTecnica"
            rows="3"
            label="Descripción Técnica"
            placeholder="Detalles técnicos de la estructura"
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

<!-- eslint-disable vue/no-mutating-props -->
<script setup>
import { computed, watch } from 'vue'
import { $api } from '@/utils/api'

const props = defineProps({
  index: {
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
  statusOptions: {
    type: Array,
    required: true,
  },
  isEditMode: {
    type: Boolean,
    required: false,
    default: false,
  },
})

const emit = defineEmits([
  'remove',
  'update:data',
  'update:status'
])

// Usar computed para los datos locales
const localProductData = computed({
  get: () => props.data,
  set: (value) => {
    emit('update:data', {
      id: props.index,
      data: value
    })
  }
})

// Observar cambios en las props para actualizar los datos locales
watch(() => props.data, (newValue) => {
  if (JSON.stringify(newValue) !== JSON.stringify(localProductData.value)) {
    emit('update:data', {
      id: props.index,
      data: newValue
    })
  }
}, { deep: true })

// Función para actualizar el estado
const updateStatus = async (newStatus) => {
  try {
    // Solo actualizamos el estado localmente
    emit('update:data', {
      id: props.index,
      data: {
        ...localProductData.value,
        status: newStatus
      }
    })
  } catch (error) {
    console.error('Error al actualizar el estado:', error)
    // En caso de error, revertimos el estado
    localProductData.value.status = props.data.status
  }
}

const removeProduct = () => {
  emit('remove', props.index)
}
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
    class="d-flex flex-sm-row flex-column-reverse mb-4"
  >
    <!-- 👉 Left Form -->
    <div class="pa-5 flex-grow-1">
      <!-- Detalles de la Orden -->
      <VRow class="mb-2">
        <VCol :cols="12" :md="isEditMode ? 4 : 6">
          <VTextField
            v-model.number="localProductData.quantity"
            type="number"
            label="Cantidad"
            min="1"
            class="mb-2"
            hide-details
          />
        </VCol>
        <VCol :cols="12" :md="isEditMode ? 4 : 6">
          <VTextField
            v-model.number="localProductData.unitprice"
            type="number"
            label="Precio Unitario"
            min="0"
            prefix="S/."
            class="mb-2"
            hide-details
          />
        </VCol>
        <VCol v-if="isEditMode" cols="12" md="4">
          <VSelect
            v-model="localProductData.status"
            :items="statusOptions"
            item-title="title"
            item-value="value"
            label="Estado"
            class="mb-2"
            hide-details
            @update:model-value="updateStatus"
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

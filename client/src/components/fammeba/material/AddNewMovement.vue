<script setup>
import { ref, onMounted } from 'vue'
import { $api } from '@/utils/api'

const props = defineProps({
  isDialogVisible: {
    type: Boolean,
    required: true,
  }
})

const emit = defineEmits(['update:isDialogVisible', 'movement-registered'])

// Estados
const material = ref(null)
const movementType = ref(null)
const quantity = ref(null)
const description = ref('')
const error = ref(null)
const success = ref(null)
const loading = ref(false)
const materials = ref([])

// Tipos de movimiento disponibles
const movementTypeOptions = [
  { title: 'Entrada', value: 'IN' },
  { title: 'Salida', value: 'OUT' },
  { title: 'Ajuste', value: 'ADJUSTMENT' },
  { title: 'Pérdida', value: 'LOSS' },
  { title: 'Devolución', value: 'RETURN' },
]

// Validaciones
const requiredSelectValidator = (value) => {
  if (!value) return 'Por favor seleccione una opción'
  return true
}

const requiredNumberValidator = (value) => {
  if (!value) return 'Este campo es requerido'
  if (value <= 0) return 'La cantidad debe ser mayor a 0'
  return true
}

const isFormValid = ref(false)
const refForm = ref()

// Cargar materiales
const fetchMaterials = async () => {
  try {
    const response = await $api('/materials?size=100', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    materials.value = response.content
  } catch (error) {
    console.error('Error al cargar materiales:', error)
  }
}

// Función para cerrar el diálogo
const closeDialog = () => {
  emit('update:isDialogVisible', false)
  refForm.value?.reset()
  refForm.value?.resetValidation()
  error.value = null
  success.value = null
}

// Función para registrar el movimiento
const registerMovement = async () => {
  refForm.value?.validate().then(async ({ valid }) => {
    if (valid) {
      loading.value = true
      error.value = null
      try {
        const movementData = {
          movementType: movementType.value,
          quantity: quantity.value,
          description: description.value,
          userId: JSON.parse(localStorage.getItem('user')).id
        }

        await $api(`/materials/movements/${material.value}`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          },
          body: movementData
        })

        success.value = 'Movimiento registrado correctamente'
        emit('movement-registered')
        
        setTimeout(() => {
          closeDialog()
        }, 1500)
      } catch (err) {
        error.value = err.response?._data?.message || 'Error al registrar el movimiento'
      } finally {
        loading.value = false
      }
    }
  })
}

// Cargar materiales al montar el componente
onMounted(() => {
  fetchMaterials()
})
</script>

<template>
  <VDialog
    :model-value="props.isDialogVisible"
    persistent
    max-width="500px"
    @update:model-value="val => emit('update:isDialogVisible', val)"
  >
    <VCard>
      <VCardText class="pt-5">
        <div class="text-center pb-6">
          <h4 class="text-h4 mb-2">
            Registrar Movimiento
          </h4>
          <div class="text-body-1">
            Ingrese los detalles del movimiento de material.
          </div>
        </div>

        <VForm
          ref="refForm"
          v-model="isFormValid"
          @submit.prevent="registerMovement"
        >
          <VRow>
            <!-- Material -->
            <VCol cols="12">
              <VSelect
                v-model="material"
                :items="materials"
                item-title="name"
                item-value="code"
                label="Material"
                placeholder="Seleccionar material"
                :rules="[requiredSelectValidator]"
                prepend-inner-icon="ri-box-3-line"
              />
            </VCol>

            <!-- Tipo de Movimiento -->
            <VCol cols="12">
              <VSelect
                v-model="movementType"
                :items="movementTypeOptions"
                label="Tipo de Movimiento"
                placeholder="Seleccionar tipo de movimiento"
                :rules="[requiredSelectValidator]"
                prepend-inner-icon="ri-exchange-line"
              />
            </VCol>

            <!-- Cantidad -->
            <VCol cols="12">
              <VTextField
                v-model="quantity"
                type="number"
                label="Cantidad"
                placeholder="Ingrese la cantidad"
                :rules="[requiredNumberValidator]"
                prepend-inner-icon="ri-scales-3-line"
              />
            </VCol>

            <!-- Descripción -->
            <VCol cols="12">
              <VTextarea
                v-model="description"
                label="Descripción"
                placeholder="Ingrese una descripción"
                rows="3"
                prepend-inner-icon="ri-file-text-line"
              />
            </VCol>

            <!-- Alerts -->
            <VCol cols="12">
              <VAlert
                v-if="error"
                type="error"
                closable
                class="mb-4"
              >
                {{ error }}
              </VAlert>

              <VAlert
                v-if="success"
                type="success"
                closable
                class="mb-4"
              >
                {{ success }}
              </VAlert>
            </VCol>

            <!-- Botones -->
            <VCol cols="12" class="d-flex justify-end gap-4">
              <VBtn
                color="error"
                variant="tonal"
                @click="closeDialog"
                prepend-icon="ri-close-circle-fill"
              >
                Cancelar
              </VBtn>
              <VBtn
                color="primary"
                :loading="loading"
                @click="registerMovement"
                prepend-icon="ri-save-line"
              >
                Registrar
              </VBtn>
            </VCol>
          </VRow>
        </VForm>
      </VCardText>
    </VCard>
  </VDialog>
</template> 
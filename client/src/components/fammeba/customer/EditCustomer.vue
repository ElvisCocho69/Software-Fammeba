<script setup>
import { PerfectScrollbar } from 'vue3-perfect-scrollbar'
import { ref, nextTick } from 'vue'

const props = defineProps({
  isDrawerOpen: {
    type: Boolean,
    required: true,
  },
  customerData: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['update:isDrawerOpen', 'customer-updated'])

const isFormValid = ref(false)
const refForm = ref()
const error = ref(null)
const success = ref(null)

const editedCustomer = ref({
  name: '',
  lastname: '',
  businessName: '',
  birthDate: '',
  documentType: '',
  documentNumber: '',
  email: '',
  address: '',
  status: 'ENABLED',
})

const documentTypes = [
  { title: 'DNI', value: 'DNI' },
  { title: 'Pasaporte', value: 'PASSPORT' },
  { title: 'Cédula de Identidad', value: 'ID_CARD' },
]

// Validaciones
const minLengthValidator = (minLength) => (value) => {
  if (!value) return 'Este campo es requerido'
  if (value.length < minLength) return `Mínimo ${minLength} caracteres`
  return true
}

const emailValidator = (value) => {
  if (!value) return 'Este campo es requerido'
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) return 'Por favor ingrese un email válido'
  return true
}

const documentNumberValidator = (value) => {
  if (!value) return 'Este campo es requerido'
  if (!/^\d+$/.test(value)) return 'El número de documento debe contener solo números'
  return true
}

const requiredValidator = (value) => {
  if (!value) return 'Este campo es requerido'
  return true
}

// 👉 drawer close
const closeNavigationDrawer = () => {
  emit('update:isDrawerOpen', false)
  nextTick(() => {
    refForm.value?.reset()
    refForm.value?.resetValidation()
    error.value = null
    success.value = null
  })
}

const onSubmit = () => {
  refForm.value?.validate().then(({ valid }) => {
    if (valid) {
      // Aquí iría la llamada a la API cuando se implemente
      console.log('Datos actualizados del cliente:', editedCustomer.value)
      
      success.value = 'Cliente actualizado correctamente'
      emit('customer-updated')
      
      setTimeout(() => {
        closeNavigationDrawer()
      }, 1500)
    }
  })
}

const handleDrawerModelValueUpdate = val => {
  emit('update:isDrawerOpen', val)
}
</script>

<template>
  <VNavigationDrawer
    temporary
    :width="400"
    location="end"
    class="scrollable-content"
    :model-value="props.isDrawerOpen"
    @update:model-value="handleDrawerModelValueUpdate"
  >
    <!-- 👉 Title -->
    <AppDrawerHeaderSection
      title="Editar Cliente"
      @cancel="closeNavigationDrawer"
    />

    <VDivider />

    <PerfectScrollbar :options="{ wheelPropagation: false }">
      <VCard flat>
        <VCardText>
          <!-- 👉 Form -->
          <VForm
            ref="refForm"
            v-model="isFormValid"
            @submit.prevent="onSubmit"
          >
            <VRow>
              <!-- 👉 First Name -->
              <VCol cols="12">
                <VTextField
                  v-model="editedCustomer.name"
                  :rules="[minLengthValidator(2)]"
                  label="Nombre"
                  placeholder="Juan"
                />
              </VCol>

              <!-- 👉 Last Name -->
              <VCol cols="12">
                <VTextField
                  v-model="editedCustomer.lastname"
                  :rules="[minLengthValidator(2)]"
                  label="Apellido"
                  placeholder="Pérez"
                />
              </VCol>

              <!-- 👉 Business Name -->
              <VCol cols="12">
                <VTextField
                  v-model="editedCustomer.businessName"
                  label="Razón Social"
                  placeholder="Empresa S.A."
                />
              </VCol>

              <!-- 👉 Birth Date -->
              <VCol cols="12">
                <VTextField
                  v-model="editedCustomer.birthDate"
                  :rules="[requiredValidator]"
                  label="Fecha de Nacimiento"
                  type="date"
                />
              </VCol>

              <!-- 👉 Document Type -->
              <VCol cols="12">
                <VSelect
                  v-model="editedCustomer.documentType"
                  :rules="[requiredValidator]"
                  label="Tipo de Documento"
                  :items="documentTypes"
                  item-title="title"
                  item-value="value"
                />
              </VCol>

              <!-- 👉 Document Number -->
              <VCol cols="12">
                <VTextField
                  v-model="editedCustomer.documentNumber"
                  :rules="[documentNumberValidator]"
                  label="Número de Documento"
                  placeholder="12345678"
                />
              </VCol>

              <!-- 👉 Email -->
              <VCol cols="12">
                <VTextField
                  v-model="editedCustomer.email"
                  :rules="[emailValidator]"
                  label="Email"
                  placeholder="juan@example.com"
                />
              </VCol>

              <!-- 👉 Address -->
              <VCol cols="12">
                <VTextField
                  v-model="editedCustomer.address"
                  label="Dirección"
                  placeholder="Calle Principal 123"
                />
              </VCol>

              <!-- 👉 Status -->
              <VCol cols="12">
                <VSelect
                  v-model="editedCustomer.status"
                  label="Estado"
                  :items="[
                    { title: 'Activo', value: 'ENABLED' },
                    { title: 'Inactivo', value: 'DISABLED' }
                  ]"
                  item-title="title"
                  item-value="value"
                />
              </VCol>

              <!-- 👉 Alerts -->
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

              <!-- 👉 Submit and Cancel -->
              <VCol cols="12">
                <VBtn
                  type="submit"
                  class="me-4"
                  prepend-icon="ri-save-line"
                >
                  Guardar
                </VBtn>
                <VBtn
                  type="reset"
                  variant="outlined"
                  color="error"
                  @click="closeNavigationDrawer"
                  prepend-icon="ri-close-fill"
                >
                  Cancelar
                </VBtn>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </PerfectScrollbar>
  </VNavigationDrawer>
</template> 
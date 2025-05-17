<script setup>
import { PerfectScrollbar } from 'vue3-perfect-scrollbar'
import { ref, nextTick, computed } from 'vue'

const props = defineProps({
  isDrawerOpen: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['update:isDrawerOpen', 'customer-created'])

const isFormValid = ref(false)
const refForm = ref()
const error = ref(null)
const success = ref(null)

const customerData = ref({
  documentType: '',
  name: '',
  lastname: '',
  businessName: '',
  birthDate: '',
  documentNumber: '',
  email: '',
  address: '',
  status: 'ENABLED',
})

const documentTypes = [
  { title: 'DNI', value: 'DNI' },
  { title: 'RUC', value: 'RUC' },
  { title: 'Pasaporte', value: 'PASSPORT' },
  { title: 'Cédula de Identidad', value: 'ID_CARD' },
]

const isRuc = computed(() => customerData.value.documentType === 'RUC')

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

const rucValidator = (value) => {
  if (!value) return 'Este campo es requerido'
  if (!/^\d{11}$/.test(value)) return 'El RUC debe tener 11 dígitos'
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
    customerData.value.documentType = ''
  })
}

const onSubmit = () => {
  refForm.value?.validate().then(({ valid }) => {
    if (valid) {
      // Aquí iría la llamada a la API cuando se implemente
      console.log('Datos del cliente:', customerData.value)
      
      success.value = 'Cliente creado correctamente'
      emit('customer-created')
      
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
      title="Añadir Cliente"
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
              <!-- 👉 Document Type -->
              <VCol cols="12">
                <VSelect
                  v-model="customerData.documentType"
                  :rules="[requiredValidator]"
                  label="Tipo de Documento"
                  :items="documentTypes"
                  item-title="title"
                  item-value="value"
                />
              </VCol>

              <template v-if="customerData.documentType">
                <template v-if="isRuc">
                  <!-- 👉 Business Name -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.businessName"
                      :rules="[requiredValidator]"
                      label="Razón Social"
                      placeholder="Empresa S.A."
                    />
                  </VCol>

                  <!-- 👉 RUC Number -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.documentNumber"
                      :rules="[rucValidator]"
                      label="Número de RUC"
                      placeholder="20123456789"
                    />
                  </VCol>

                  <!-- 👉 Address -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.address"
                      :rules="[requiredValidator]"
                      label="Dirección"
                      placeholder="Calle Principal 123"
                    />
                  </VCol>
                </template>

                <template v-else>
                  <!-- 👉 First Name -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.name"
                      :rules="[minLengthValidator(2)]"
                      label="Nombre"
                      placeholder="Juan"
                    />
                  </VCol>

                  <!-- 👉 Last Name -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.lastname"
                      :rules="[minLengthValidator(2)]"
                      label="Apellido"
                      placeholder="Pérez"
                    />
                  </VCol>

                  <!-- 👉 Document Number -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.documentNumber"
                      :rules="[documentNumberValidator]"
                      label="Número de Documento"
                      placeholder="12345678"
                    />
                  </VCol>

                  <!-- 👉 Birth Date -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.birthDate"
                      :rules="[requiredValidator]"
                      label="Fecha de Nacimiento"
                      type="date"
                    />
                  </VCol>

                  <!-- 👉 Email -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.email"
                      :rules="[emailValidator]"
                      label="Email"
                      placeholder="juan@example.com"
                    />
                  </VCol>

                  <!-- 👉 Address -->
                  <VCol cols="12">
                    <VTextField
                      v-model="customerData.address"
                      label="Dirección"
                      placeholder="Calle Principal 123"
                    />
                  </VCol>
                </template>

                <!-- 👉 Status -->
                <VCol cols="12">
                  <VSelect
                    v-model="customerData.status"
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
              </template>

              <!-- 👉 Submit and Cancel -->
              <VCol cols="12">
                <VBtn
                  type="submit"
                  class="me-4"
                  prepend-icon="ri-user-add-line"
                >
                  Añadir
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
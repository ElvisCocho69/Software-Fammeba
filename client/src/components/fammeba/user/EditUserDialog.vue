<script setup>
import { PerfectScrollbar } from 'vue3-perfect-scrollbar'
import { $api } from '@/utils/api'
import { ref, onMounted, watch } from 'vue'

const props = defineProps({
  isDrawerOpen: {
    type: Boolean,
    required: true,
  },
  userData: {
    type: Object,
    required: false,
    default: () => ({})
  }
})

const emit = defineEmits([
  'update:isDrawerOpen',
  'userData',
  'user-created'
])

// Validaciones 
const minLengthValidator = (minLength) => (value) => {
  if (!value) return 'Este campo es requerido'
  if (value.length < minLength) return `Mínimo ${minLength} caracteres`
  return true
}

const contactValidator = (value) => {
  if (!value) return 'Este campo es requerido'
  if (!/^\d{9}$/.test(value)) return 'El contacto debe tener exactamente 9 dígitos'
  return true
}

// Validador de contraseña repetida: solo obligatorio si password tiene valor
const passwordMatchValidator = (value) => {
  if (password.value) {
    if (!value) return 'Debe confirmar la contraseña'
    if (value !== password.value) return 'Las contraseñas no coinciden'
  }
  return true
}

const passwordOptionalValidator = (value) => {
  if (value && value.length < 8) return 'Mínimo 8 caracteres'
  return true
}

const emailValidator = (value) => {
  if (!value) return 'Este campo es requerido'
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) return 'Por favor ingrese un email válido'
  return true
}

const requiredSelectValidator = (value) => {
  if (!value) return 'Por favor seleccione una opción'
  return true
}

const isFormValid = ref(false)
const refForm = ref()
const name = ref('')
const lastname = ref('')
const email = ref('')
const contacto = ref('')
const username = ref('')
const password = ref('')
const repeatedPassword = ref('')
const role = ref()
const status = ref()
const roles = ref([])
const error = ref(null)
const success = ref(null)
const showPassword = ref(false)
const showRepeatedPassword = ref(false)

// Estados disponibles
const statusOptions = [
  { title: 'Activo', value: 'ENABLED' },
  { title: 'Inactivo', value: 'DISABLED' },
]

// Cargar roles
onMounted(async () => {
  try {
    const response = await $api('/roles', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    roles.value = response.content.map(role => ({
      title: role.name,
      value: role.name
    }))
  } catch (error) {
    console.error('Error al cargar roles:', error)
  }
})

watch(
  () => props.userData,
  (newUser) => {
    if (newUser && newUser.id) {
      name.value = newUser.name || ''
      lastname.value = newUser.lastname || ''
      email.value = newUser.email || ''
      contacto.value = newUser.contacto || ''
      username.value = newUser.username || ''
      role.value = newUser.role || ''
      status.value = newUser.status || ''
      password.value = ''
      repeatedPassword.value = ''
    }
  },
  { immediate: true }
)

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

const onSubmit = async () => {
  refForm.value?.validate().then(async ({ valid }) => {
    if (valid) {
      try {
        const userData = {
          name: name.value,
          lastname: lastname.value,
          email: email.value,
          contacto: contacto.value,
          username: username.value,
          status: status.value,
          role: role.value
        }
        if (password.value) {
          userData.password = password.value
        }
        const response = await $api(`/users/${props.userData.id}`, {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: userData
        })
        success.value = 'Usuario actualizado correctamente'
        emit('userData', response)
        emit('user-created')
        setTimeout(() => {
          closeNavigationDrawer()
        }, 1500)
      } catch (err) {
        error.value = err.response?._data?.message || 'Error al actualizar el usuario'
      }
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
      title="Actualizar Usuario"
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
              <!-- 👉 Name -->
              <VCol cols="12">
                <VTextField
                  v-model="name"
                  :rules="[minLengthValidator(4)]"
                  label="Nombres"
                  placeholder="Juan"
                />
              </VCol>

              <!-- 👉 Lastname -->
              <VCol cols="12">
                <VTextField
                  v-model="lastname"
                  :rules="[minLengthValidator(4)]"
                  label="Apellidos"
                  placeholder="Pérez"
                />
              </VCol>
              
              <!-- 👉 Email -->
              <VCol cols="12">
                <VTextField
                  v-model="email"
                  :rules="[emailValidator]"
                  label="Email"
                  placeholder="juan@mail.com"
                />
              </VCol>

              <!-- 👉 Contact -->
              <VCol cols="12">
                <VTextField
                  v-model="contacto"
                  :rules="[contactValidator]"
                  label="Contacto"
                  placeholder="123456789"
                />
              </VCol>

              <!-- 👉 Username -->
              <VCol cols="12">
                <VTextField
                  v-model="username"
                  :rules="[minLengthValidator(4)]"
                  label="Usuario"
                  placeholder="juanp"
                />
              </VCol>

              <!-- 👉 Password -->
              <VCol cols="12">
                <VTextField
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  :rules="[passwordOptionalValidator]"
                  label="Contraseña (opcional)"
                  placeholder="********"
                  :append-inner-icon="showPassword ? 'ri-eye-off-line' : 'ri-eye-line'"
                  @click:append-inner="showPassword = !showPassword"
                />
              </VCol>

              <!-- 👉 Repeat Password -->
              <VCol cols="12">
                <VTextField
                  v-model="repeatedPassword"
                  :type="showRepeatedPassword ? 'text' : 'password'"
                  :rules="[passwordMatchValidator]"
                  label="Confirmar Contraseña"
                  placeholder="********"
                  :append-inner-icon="showRepeatedPassword ? 'ri-eye-off-line' : 'ri-eye-line'"
                  @click:append-inner="showRepeatedPassword = !showRepeatedPassword"
                />
              </VCol>
              
              <!-- 👉 Role -->
              <VCol cols="12">
                <VSelect
                  v-model="role"
                  label="Seleccionar Rol"
                  placeholder="Seleccionar Rol"
                  :rules="[requiredSelectValidator]"
                  :items="roles"
                />
              </VCol>             

              <!-- 👉 Status -->
              <VCol cols="12">
                <VSelect
                  v-model="status"
                  label="Seleccionar Estado"
                  placeholder="Seleccionar Estado"
                  :rules="[requiredSelectValidator]"
                  :items="statusOptions"
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
                  prepend-icon="ri-user-add-line"
                >
                  Actualizar
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

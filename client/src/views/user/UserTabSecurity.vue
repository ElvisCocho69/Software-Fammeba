<script setup>

const isNewPasswordVisible = ref(false)
const isConfirmPasswordVisible = ref(false)
 
</script>

<template>
  <VRow>
    <VCol cols="12">
      <!-- 👉 Change password -->
      <VCard title="Cambiar Contraseña">
        <VCardText>
          <VAlert
            variant="tonal"
            color="warning"
            closable
            class="mb-4"
          >
            <VAlertTitle>Asegúrese de que se cumplan estos requisitos</VAlertTitle>
            <span>Mínimo 8 caracteres</span>
          </VAlert>

          <VForm @submit.prevent="() => {}">
            <VRow>
              <VCol
                cols="12"
                md="6"
              >
                <VTextField
                  label="Nueva Contraseña"
                  placeholder="············"
                  :type="isNewPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isNewPasswordVisible ? 'ri-eye-off-line' : 'ri-eye-line'"
                  @click:append-inner="isNewPasswordVisible = !isNewPasswordVisible"
                />
              </VCol>
              <VCol
                cols="12"
                md="6"
              >
                <VTextField
                  label="Confirmar Contraseña"
                  placeholder="············"
                  :type="isConfirmPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isConfirmPasswordVisible ? 'ri-eye-off-line' : 'ri-eye-line'"
                  @click:append-inner="isConfirmPasswordVisible = !isConfirmPasswordVisible"
                />
              </VCol>

              <VCol cols="12">
                <VBtn 
                  type="submit"
                  prepend-icon="ri-key-fill"
                  >
                  Confirmar
                </VBtn>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </VCol>    
  </VRow>

  <!-- 👉 Enable One Time Password Dialog -->
  
</template>

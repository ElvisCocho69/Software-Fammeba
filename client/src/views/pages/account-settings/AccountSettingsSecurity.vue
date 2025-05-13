<script setup>
import boyWithTab from '@images/illustrations/account-settings-security-illustration.png'

const isCurrentPasswordVisible = ref(false)
const isNewPasswordVisible = ref(false)
const isConfirmPasswordVisible = ref(false)
const currentPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')

const passwordRequirements = [
  'Minimum 8 characters long - the more, the better',
  'At least one lowercase character',
  'At least one number, symbol, or whitespace character',
]


</script>

<template>
  <VRow>
    <!-- SECTION: Change Password -->
    <VCol cols="12">
      <VCard>
        <VCardItem class="pb-6">
          <VCardTitle>Change Password</VCardTitle>
        </VCardItem>
        <VForm>
          <VCardText class="pt-0">
            <!-- 👉 Current Password -->
            <VRow>
              <VCol
                cols="12"
                md="6"
              >
                <!-- 👉 current password -->
                <VTextField
                  v-model="currentPassword"
                  :type="isCurrentPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isCurrentPasswordVisible ? 'ri-eye-off-line' : 'ri-eye-line'"
                  autocomplete="on"
                  label="Current Password"
                  placeholder="············"
                  @click:append-inner="isCurrentPasswordVisible = !isCurrentPasswordVisible"
                />
              </VCol>
            </VRow>

            <!-- 👉 New Password -->
            <VRow>
              <VCol
                cols="12"
                md="6"
              >
                <!-- 👉 new password -->
                <VTextField
                  v-model="newPassword"
                  :type="isNewPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isNewPasswordVisible ? 'ri-eye-off-line' : 'ri-eye-line'"
                  label="New Password"
                  autocomplete="on"
                  placeholder="············"
                  @click:append-inner="isNewPasswordVisible = !isNewPasswordVisible"
                />
              </VCol>

              <VCol
                cols="12"
                md="6"
              >
                <!-- 👉 confirm password -->
                <VTextField
                  v-model="confirmPassword"
                  :type="isConfirmPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isConfirmPasswordVisible ? 'ri-eye-off-line' : 'ri-eye-line'"
                  autocomplete="on"
                  label="Confirm New Password"
                  placeholder="············"
                  @click:append-inner="isConfirmPasswordVisible = !isConfirmPasswordVisible"
                />
              </VCol>
            </VRow>
          </VCardText>

          <!-- 👉 Password Requirements -->
          <VCardText>
            <h6 class="text-h6 text-medium-emphasis mt-1">
              Password Requirements:
            </h6>

            <VList>
              <VListItem
                v-for="(item, index) in passwordRequirements"
                :key="index"
                class="px-0"
              >
                <template #prepend>
                  <VIcon
                    size="8"
                    icon="ri-circle-fill"
                    color="rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity))"
                  />
                </template>
                <VListItemTitle class="text-medium-emphasis text-wrap">
                  {{ item }}
                </VListItemTitle>
              </VListItem>
            </VList>

            <!-- 👉 Action Buttons -->
            <div class="d-flex flex-wrap gap-4 mt-2">
              <VBtn>Save changes</VBtn>

              <VBtn
                type="reset"
                color="secondary"
                variant="outlined"
              >
                Reset
              </VBtn>
            </div>
          </VCardText>
        </VForm>
      </VCard>
    </VCol>    
    
  </VRow>

</template>

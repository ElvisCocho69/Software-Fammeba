<script setup>
import { useGenerateImageVariant } from '@/@core/composable/useGenerateImageVariant'
import authV2ForgotPasswordIllustrationBorderedDark from '@images/pages/auth-v2-forgot-password-illustration-bordered-dark.png'
import authV2ForgotPasswordIllustrationBorderedLight from '@images/pages/auth-v2-forgot-password-illustration-bordered-light.png'
import authV2ForgotPasswordIllustrationDark from '@images/pages/auth-v2-forgot-password-illustration-dark.png'
import authV2ForgotPasswordIllustrationLight from '@images/pages/auth-v2-forgot-password-illustration-light.png'
import authV2ForgotPasswordMaskDark from '@images/pages/auth-v2-forgot-password-mask-dark.png'
import authV2ForgotPasswordMaskLight from '@images/pages/auth-v2-forgot-password-mask-light.png'
import { VNodeRenderer } from '@layouts/components/VNodeRenderer'
import { themeConfig } from '@themeConfig'

const authV2ForgotPasswordMask = useGenerateImageVariant(authV2ForgotPasswordMaskLight, authV2ForgotPasswordMaskDark)
const authV2ForgotPasswordIllustration = useGenerateImageVariant(authV2ForgotPasswordIllustrationLight, authV2ForgotPasswordIllustrationDark, authV2ForgotPasswordIllustrationBorderedLight, authV2ForgotPasswordIllustrationBorderedDark, true)
const email = ref('')

definePage({ meta: { layout: 'blank' } })
</script>

<template>
  <RouterLink to="/">
    <div class="app-logo auth-logo">
      <VNodeRenderer :nodes="themeConfig.app.logo" />
      <h1 class="app-logo-title">
        {{ themeConfig.app.title }}
      </h1>
    </div>
  </RouterLink>

  <VRow
    class="auth-wrapper"
    no-gutters
  >
    <VCol
      md="8"
      class="d-none d-md-flex align-center justify-center position-relative"
    >
      <div class="d-flex align-center justify-center pa-10">
        <img
          :src="authV2ForgotPasswordIllustration"
          class="auth-illustration w-100"
          alt="auth-illustration"
        >
      </div>
      <VImg
        :src="authV2ForgotPasswordMask"
        class="d-none d-md-flex auth-footer-mask"
        alt="auth-mask"
      />
    </VCol>

    <VCol
      cols="12"
      md="4"
      class="auth-card-v2 d-flex align-center justify-center"
      style="background-color: rgb(var(--v-theme-surface));"
    >
      <VCard
        flat
        :max-width="500"
        class="mt-12 mt-sm-0 pa-5 pa-lg-7"
      >
        <VCardText>
          <h4 class="text-h4 mb-1">
            Forgot Password? 🔒
          </h4>
          <p class="mb-0">
            Enter your email and we'll send you instructions to reset your password
          </p>
        </VCardText>

        <VCardText>
          <VForm @submit.prevent="() => {}">
            <VRow>
              <!-- email -->
              <VCol cols="12">
                <VTextField
                  v-model="email"
                  autofocus
                  label="Email"
                  placeholder="johndoe@email.com"
                  type="email"
                />
              </VCol>

              <!-- Reset link -->
              <VCol cols="12">
                <VBtn
                  block
                  type="submit"
                >
                  Send Reset Link
                </VBtn>
              </VCol>

              <!-- back to login -->
              <VCol cols="12">
                <RouterLink
                  class="d-flex align-center justify-center"
                  :to="{ name: 'pages-authentication-login-v2' }"
                >
                  <VIcon
                    start
                    icon="ri-arrow-left-s-line"
                    size="20"
                    class="flip-in-rtl"
                  />
                  <span class="text-base">Back to login</span>
                </RouterLink>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </VCol>
  </VRow>
</template>

<style lang="scss">
@use "@core/scss/template/pages/page-auth.scss";
</style>

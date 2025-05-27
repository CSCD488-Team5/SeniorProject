<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import TokenService from "@/scripts/TokenService.js";

const router = useRouter();
const username = ref('');
const password = ref('');
const message = ref('');
const form = ref(null);

const clientId = import.meta.env.VITE_CAMPUSHIVE_CLIENTID;
const tenantId = 'common';
const redirectUrl = import.meta.env.VITE_REDIRECT_URL;


const redirectToMicrosoftSSO = () => {
  const url = `https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=${clientId}` +
              `&response_type=token` +
              `&redirect_uri=${encodeURIComponent(redirectUrl)}` +
              `&scope=openid%20profile%20email`+
              `&prompt=select_account`;
  console.log("Client ID:", clientId);
  console.log("Redirect URL:", redirectUrl);


  window.location.href = url;
};


// Validation rules
const required = (v) => !!v || 'This field is required';

const login = async () => {
  const isValid = await form.value.validate();

  if (!isValid.valid) {
    message.value = "Please fill in all required fields.";
    return;
  }

  try {
    const response = await axios.post('http://localhost/api/auth/login', {
      username: username.value,
      password: password.value
    });
    TokenService.saveToken(response.data);
    window.dispatchEvent(new Event("user-logged-in"));
    message.value = "Logged in! Token saved.";
    router.push('/Home');
  } catch (error) {
    message.value = error.response?.data?.message || 'Login failed';
    console.error('Login error:', error);
  }
};
</script>

<template>
  <v-container class="fill-height d-flex align-center justify-center">
    <v-card class="pa-6" width="400" elevation="8">
      <v-card-title class="text-h5 text-center">Login</v-card-title>

      <v-form @submit.prevent="login" ref="form">
        <v-text-field
          v-model="username"
          label="Username"
          prepend-inner-icon="mdi-account"
          variant="outlined"
          :rules="[required]"
        />

        <v-text-field
          v-model="password"
          label="Password"
          prepend-inner-icon="mdi-lock"
          variant="outlined"
          type="password"
          :rules="[required]"
        />

        <v-btn
          type="submit"
          color="primary"
          block
          class="mt-4"
        >
          Login
        </v-btn>

        <v-btn
          block
          color="info"
          class="mt-2"
          prepend-icon="mdi-microsoft"
          @click="redirectToMicrosoftSSO">
          Continue with Microsoft
        </v-btn>

        <v-alert
          v-if="message"
          type="info"
          class="mt-4"
          border="start"
        >
          {{ message }}
        </v-alert>
      </v-form>

      <v-card-actions class="justify-end mt-4">
        <v-btn
          variant="tonal"
          color="secondary"
          to="/signup"
        >
          Sign Up
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";
import { useRouter } from 'vue-router';
import TokenService from '@/scripts/TokenService';

const router = useRouter();
const name = ref('');
const username = ref('');
const email = ref('');
const password = ref('');
const message = ref('');
const form = ref(null);

// Validation rules
const required = v => !!v || 'This field is required';
const validEmail = v => /.+@.+\..+/.test(v) || 'E-mail must be valid';

const signup = async () => {

  const isValid = await form.value.validate();

if (!isValid.valid) {
  message.value = "Please fix the errors above.";
  return;
}

  try {
    await axios.post('http://localhost/api/auth/signup', {
      name: name.value,
      username: username.value,
      email: email.value,
      password: password.value
    });

    const response = await axios.post('http://localhost/api/auth/login', {
      username: username.value,
      password: password.value
    });

    const token = response.data;
    if (!token || typeof token !== 'string') {
      throw new Error("Invalid token format");
    }

    TokenService.saveToken(token);
    window.dispatchEvent(new Event("user-logged-in"));

    router.push('/Home');
  } catch (error) {
    message.value = 'Error: ' + (error.response?.data?.message || 'Signup/Login failed');
    console.error('Signup/Login error:', error);
  }
};
</script>

<template>
  <v-container class="fill-height d-flex align-center justify-center">
    <v-card class="pa-6" width="400" elevation="8">
      <v-card-title class="text-h5 text-center">Sign Up</v-card-title>

      <v-form @submit.prevent="signup" ref="form">
        <v-text-field
          v-model="name"
          label="Name"
          prepend-inner-icon="mdi-account-outline"
          variant="outlined"
          :rules="[required]"
        />
        <v-text-field
          v-model="username"
          label="Username"
          prepend-inner-icon="mdi-account"
          variant="outlined"
          :rules="[required]"
        />
        <v-text-field
          v-model="email"
          label="Email"
          prepend-inner-icon="mdi-email"
          variant="outlined"
          type="email"
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
          Sign Up
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
          to="/login"
        >
          Login
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

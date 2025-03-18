<script setup>
import { ref } from 'vue';

const username = ref('');
const password = ref('');
const message = ref('');

const login = async () => {
  try {
    const response = await fetch('http://localhost/api/SignupController/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: username.value, password: password.value })
    });

    const data = await response.text();

    if (!response.ok) {
      throw new Error(data || 'Login failed');
    }

    message.value = `✅ ${data}`; // Success message
  } catch (error) {
    message.value = `❌ ${error.message}`; // Error message
  }
};
</script>

<template>
  <div class="container">
    <div class="top-right">
      <router-link to="/Signup" class="signup-button">Sign Up</router-link>
    </div>
    <div class="login-box">
      <h2 class="title">Login</h2>
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">Username</label>
          <input
              type="username"
              id="username"
              v-model="username"
              class="input-field"
              placeholder="Enter your username"
              required
          />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
              type="password"
              id="password"
              v-model="password"
              class="input-field"
              placeholder="Enter your password"
              required
          />
        </div>
        <button type="submit" class="login-button">Login</button>
      </form>
      <p v-if="message" class="message">{{ message }}</p>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f3f4f6;
  position: relative;
}

.top-right {
  position: absolute;
  top: 20px;
  right: 20px;
}

.signup-button {
  padding: 0.5rem 1rem;
  background-color: #10b981;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.signup-button:hover {
  background-color: #059669;
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
}

.title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.input-field {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
}

.login-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-button:hover {
  background-color: #2563eb;
}

.message {
  margin-top: 1rem;
  text-align: center;
  font-size: 1rem;
}
</style>

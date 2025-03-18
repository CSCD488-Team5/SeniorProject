<script setup>
import { ref } from 'vue';

const name = ref('');
const username = ref('');
const email = ref('');
const password = ref('');
const message = ref('');

const signup = async () => {
  try {
    const response = await fetch('http://localhost/api/SignupController', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: name.value, username: username.value, email: email.value, password: password.value })
    });

    if (!response.ok) {
      throw new Error('Signup failed');
    }

    const data = await response.text();
    message.value = data; // Display backend response
  } catch (error) {
    message.value = 'Error: ' + error.message;
  }
};
</script>

<template>
  <div class="container">
    <div class="top-right">
      <router-link to="/login" class="signup-button">Login</router-link>
    </div>
    <div class="signup-box">
      <h2 class="title">Sign Up</h2>
      <form @submit.prevent="signup">
        <div class="form-group">
          <label for="name">Name</label>
          <input
              type="text"
              id="name"
              v-model="name"
              class="input-field"
              placeholder="Enter your name"
              required
          />
        </div>
        <div class="form-group">
          <label for="username">Username</label>
          <input
              type="text"
              id="username"
              v-model="username"
              class="input-field"
              placeholder="Enter a username"
              required
          />
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input
              type="email"
              id="email"
              v-model="email"
              class="input-field"
              placeholder="Enter your email"
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
        <button type="submit" class="signup-button">Sign Up</button>
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
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  text-decoration: none;
  transition: background-color 0.3s;
}

.signup-button:hover {
  background-color: #2563eb;
}

.signup-box {
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

.message {
  margin-top: 1rem;
  text-align: center;
  font-size: 1rem;
  color: green;
}
</style>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="w-full max-w-md p-8 space-y-6 bg-white rounded-2xl shadow-lg">
      <h2 class="text-2xl font-semibold text-center">Sign In</h2>
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label for="email" class="block text-sm font-medium">Email</label>
          <input
              v-model="email"
              type="email"
              id="email"
              required
              class="w-full p-3 mt-1 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter your email"
          />
        </div>

        <div>
          <label for="password" class="block text-sm font-medium">Password</label>
          <input
              v-model="password"
              type="password"
              id="password"
              required
              class="w-full p-3 mt-1 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter your password"
          />
        </div>

        <button
            type="submit"
            class="w-full p-3 text-white bg-blue-600 rounded-lg hover:bg-blue-700"
        >
          Sign In
        </button>
      </form>

      <p class="text-sm text-center text-gray-600">
        Don't have an account?
        <a href="/signup" class="text-blue-600 hover:underline">Sign up</a>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      password: ''
    };
  },
  methods: {
    handleSubmit() {
      console.log('Email:', this.email);
      console.log('Password:', this.password);
      // Add your authentication logic here
      alert('Sign in successful!');
    }
  }
};
</script>

<style scoped>
body {
  margin: 0;
  font-family: Arial, sans-serif;
}
</style>

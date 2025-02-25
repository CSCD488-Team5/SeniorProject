<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const msg = ref('');
const router = useRouter();

onMounted(async () => {
  try {
    const response = await fetch("http://localhost:8080/api/hello");
    msg.value = await response.text();
  } catch (error) {
    console.error("Fetch error:", error);
  }
});

// Navigate to "Find Me" page
const goToFindMe = () => {
  router.push('/find-me');
};
</script>

<template>
  <div>
    <h1>{{ msg }}</h1>
    <button @click="goToFindMe">Click Me to Find Me</button>
  </div>
</template>
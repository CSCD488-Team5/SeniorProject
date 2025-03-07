<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import Login from './components/Login.vue';
import Signup from './components/Signup.vue'

export default {
  components: {
    Login,
    Signup,
  },
};
</script>

<style>
.app{
  background-color: white;
}
</style>

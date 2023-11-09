<template>
  <div class="login">
    <h2>Login {{ Login() }}</h2>
    <h2>Access Token {{ AccessToken() }}</h2>
    <button @click="LogOut">Log Out</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import KeyCloakService from "@/services/security/KeycloakService";

export default defineComponent({
  name: "Login",
  methods: {
    Login() {
      return KeyCloakService.GetUserName();
    },
    AccessToken() {
      return KeyCloakService.GetAccesToken();
    },
    LogOut() {
      return KeyCloakService.CallLogOut();
    },
  },
});
</script>

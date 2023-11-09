<template>
  <div class="login">
    <h2>Login {{ Login() }}</h2>
    <h2>Roles {{ UserRoles()?.join(" ") }}</h2>
    <h2>Access Token {{ AccessToken() }}</h2>
    <button @click="LogOut">Log Out</button>
    <button @click="Weather">Weather</button>
  </div>
</template>

<script lang="ts">
import BudgetService from "@/services/BudgetService";
import { defineComponent } from "vue";
import KeycloakService from "@/services/security/KeycloakService";

export default defineComponent({
  name: "Login",
  methods: {
    Login() {
      return KeycloakService.GetUserName();
    },
    AccessToken() {
      return KeycloakService.GetAccesToken();
    },
    LogOut() {
      return KeycloakService.CallLogOut();
    },
    UserRoles() {
      return KeycloakService.GetUserRoles();
    },
    Weather() {
      BudgetService.getDefaultBudget()
        .then(
          (p) => alert(JSON.stringify(p)),
          (e) => alert(e.message)
        );
    },
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
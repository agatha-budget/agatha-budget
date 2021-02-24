<template>
  <div class="hello">
    <h1>{{ month }}</h1>
    <p>
      Budget of today
    </p>
    <h3>Spent</h3>
    <ul>
      <li>babel</li>
      <li>babel 1</li>
      <li>babel 2</li>
      <li>babel 3</li>
      <li>babel 4</li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'Budget',
  props: {
    month: String
  }
})
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
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

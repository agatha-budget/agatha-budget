<template>
  <div>
    <h1 class="title">Default theme</h1>
    <btn class="navigationButton">Navigation button</btn>
    <btn class="navigationButton disabled">Navigation button</btn>
  </div>

  <div class="dyslexia">
    <h1 class="title">dyslexia</h1>
    <div class="row">
    <btn class="navigationButton col-6 offset-2">Navigation button</btn>
    <btn class="navigationButton disabled col-6">Navigation button</btn>
    </div>
  </div>

</template>
<style lang="less">
  @import "../style/atomic_design/atomic_design.less";
</style>

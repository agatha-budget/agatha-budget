<template>
  <div>
    <h1 class="title">Default theme</h1>
    <btn class="navigationButton">Navigation button</btn>
    <btn class="navigationButton disabled">Navigation button</btn>

    <btn class="illustratedNavigationBtn disabled">
      <div class="icone fas fa-book col-4"></div>
      <div class="label col-8">{{ $t("FREE_RESOURCES") }}</div>
    </btn>

  </div>

  <div class="dyslexia">
    <h1 class="title">dyslexia</h1>
    <div class="row">
    <btn class="navigationButton col-6 offset-2">Navigation button</btn>
    <btn class="navigationButton disabled col-6">Navigation button</btn>
    </div>
  </div>
<br>
  <div>
    <btn class="actionButton">Valider</btn>
    <btn class="actionButton disabled">Valider</btn>
  </div>
<br>
  <div>
    <btn class="illustratedNavButton">Button</btn>
    <btn class="illustratedNavButton disabled">Button</btn>
  </div>
<br>
<div>
    <btn class="dualTabButton">Button</btn>
    <btn class="dualTabButton disabled">Button</btn>
  </div>

</template>
<style lang="less">
  @import "../style/atomic_design/atomic_design.less";
</style>

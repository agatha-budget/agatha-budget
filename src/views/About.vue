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
<h2>Action Button</h2>
  <div>
    <btn class="actionButton">Valider</btn>
    <btn class="actionButton disabled">Valider</btn>
  </div>

  <div>
    <btn class="illustratedNavButton">Button</btn>
    <btn class="illustratedNavButton disabled">Button</btn>
  </div>

<div class="dualTab">
    <btn class="tabLest" >TabLeft</btn>
    <btn class="tabRight">TabRight</btn>
  </div>

  <div>
  <input type="radio" id="huey" name="drone" value="huey"
         checked>
  <label for="huey">Radio</label>
  <input type="radio" id="huey" name="drone" value="huey"
         unchecked>
  <label for="huey">Radio</label>
</div>

<label for="pet-select">Select</label>

<select id="select">
    <option value="">Select</option>
    <option value="dog">1</option>
    <option value="cat">2</option>
    <option value="hamster">3</option>
    <option value="parrot">4</option>
    <option value="spider">5</option>
    <option value="goldfish">6</option>
</select>

</template>
<style lang="less">
  @import "../style/atomic_design/atomic_design.less";
</style>

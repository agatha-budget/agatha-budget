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
  </div>
<h2>illustratedNavButton</h2>
  <div>
    <btn class="illustratedNavButton">Button</btn>
    <btn class="illustratedNavButton disabled">Button</btn>
  </div>
<h2>dualTab</h2>
<div class="dualTab">
    <btn class="tabLest" >TabLeft</btn>
    <btn class="tabRight">TabRight</btn>
  </div>

  <div class="dualTab">
    <div class="active">
    <btn class="tabLest" >TabLeft</btn>
    <btn class="tabRight">TabRight</btn>
    </div>
  </div>

  <div class="dualTab">
    <div class="inactive">
    <btn class="tabLest" >TabLeft</btn>
    <btn class="tabRight">TabRight</btn>
    </div>
  </div>
<h2>radio</h2>
  <div>
  <input type="radio" id="huey" name="drone" value="huey"
         checked>
  <label for="huey">Radio</label>
  <input type="radio" id="huey" name="drone" value="huey"
         unchecked>
  <label for="huey">Radio</label>
</div>
<h2>switch</h2>
<div>
<label class="switch"><input class="switch-input" type="checkbox"><span class="switch-label" data-on="+" data-off="-" style="border-radius: 8px;"></span><span class="switch-handle"></span></label>
<input class="switch-input" type="checkbox">
<span class="switch-label" data-on="+" data-off="-" style="border-radius: 8px;"></span>
</div>
<div>
  <label for="switch"></label>
</div>
<h2>select</h2>
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

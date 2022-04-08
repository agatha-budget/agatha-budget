<template>
<h1 class="title">Molecules</h1>
<div class="designContainer col-8 offset-2 row">
  <h2>Button</h2>
  <div class="col-md-6">

    <h3>NavigationButtons</h3>
    <btn class="navigationButton">Navigation button</btn>

    <btn class="navigationButton disabled">Navigation button</btn>

    <h3>IllustratedNavigationButtons </h3>
    <btn class="navigationButton">
      <span class="illustration fas fa-book"/>
      {{ $t("FREE_RESOURCES") }}
    </btn>

    <btn class="navigationButton disabled">
      <span class="illustration fas fa-book"/>
      {{ $t("FREE_RESOURCES") }}
    </btn>

    <h3>Action Button</h3>
    <btn class="actionButton">Valider</btn>

  </div>
  <div class="col-md-6">

    <h3>dualTab</h3>

    <div class="dualTab">
      <btn class="tabLeft" >TabLeft</btn>
      <btn class="tabRight">TabRight</btn>
    </div>

    <div class="dualTab">
      <div class="active">
      <btn class="tabLeft" >TabLeft</btn>
      <btn class="tabRight">TabRight</btn>
      </div>
    </div>

    <div class="dualTab">
      <div class="inactive">
      <btn class="tabLeft" >TabLeft</btn>
      <btn class="tabRight">TabRight</btn>
      </div>
    </div>
  </div>
</div>

<div class="designContainer col-8 offset-2 row">
  <h2>Forms</h2>
  <div class="col-md-6">
    <h3>radio</h3>
      <div>
      <input type="radio" id="huey" name="drone" value="huey"
            checked>
      <label for="huey">Radio</label>
      <input type="radio" id="huey" name="drone" value="huey"
            unchecked>
      <label for="huey">Radio</label>
    </div>
    <h3>switch</h3>
    <div>
    <label class="switch"><input class="switch-input" type="checkbox"><span class="switch-label" data-on="+" data-off="-" style="border-radius: 8px;"></span><span class="switch-handle"></span></label>
    <input class="switch-input" type="checkbox">
    <span class="switch-label" data-on="+" data-off="-" style="border-radius: 8px;"></span>
    </div>
    <div>
      <label for="switch"></label>
    </div>
    <h3>select</h3>
    <div>
    <label>Select</label>
    <select id="select">
        <option value="">Select</option>
        <option>1</option>
        <option>2</option>
        <option>3</option>
        <option>4</option>
        <option>5</option>
        <option>6</option>
    </select>
    </div>
  </div>
  <h3>Text Input</h3>
  <label for="name">Name (4 to 8 characters):</label>
  <input type="text" id="name" name="name" required minlength="4" maxlength="8" size="10">
</div>

<h1 class="title">Organisms</h1>
<div class="designContainer col-8 offset-2 row">
  <h2>Navigation</h2>
  <div class="col-md-6">
    <h3>NavBar</h3>
    <div class="navigationMenu">
      <ul id="actionsList" class="list-group list-group-horizontal d-flex">
        <li v-if="this.fromPage == 'home' "><button class="btn fas fa-home disabled" :title="$t('HOME')"/></li>
        <li v-else><button v-on:click="goHomePage" class="btn fas fa-home" :title="$t('HOME')"/></li>
        <li v-if="this.fromPage == 'accounts' "><button class="btn fas fa-euro-sign disabled" :title="$t('ACCOUNTS')"/></li>
        <li v-else><button v-on:click="goChooseAccount" class="btn fas fa-euro-sign" :title="$t('ACCOUNTS')"/></li>
        <li v-if="this.fromPage == 'profile' "><button class="btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
        <li v-else><button v-on:click="goProfilePage" class="btn fas fa-cog" :title="$t('PREFERENCES')"/></li>
        <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
      </ul>
    </div>
  </div>
</div>
</template>
<style lang="less">
  @import "../style/atomic_design/atomic_design.less";
</style>

<template>
<h1 class="title">Molecules</h1>
<div class="designContainer col-8 offset-2 row">
  <h2>Button</h2>
  <div class="col-md-6">
    <h3>NavBarButtons</h3>
    <button class="navBarBtn btn fas fa-home" :title="$t('HOME')"/>
    <button class="navBarBtn btn fas fa-home disabled" :title="$t('HOME')"/>

    <h3>NavigationButtons</h3>
    <btn class="navigationButton">navigation button</btn>

    <btn class="navigationButton disabled">navigation button</btn>

    <h3>IllustratedNavigationButtons </h3>
    <btn class="navigationButton row">
      <span class="illustration fas fa-book col-4"/>
      <span class="illustrationLabel col-8">{{ $t("FREE_RESOURCES") }}</span>
    </btn>

    <btn class="navigationButton disabled row">
      <span class="illustration fas fa-book col-4"/>
      <span class="illustrationLabel col-8">{blbnnilbbibi</span>
    </btn>

    <h3>Action Button</h3>
    <btn class="actionButton">Valider</btn>

  </div>
  <div class="col-md-6">

    <h3>DualTab</h3>

    <div class="dualTab">
      <btn class="tabLeft" >TabLeft</btn>
      <btn class="tabRight">TabRight</btn>
    </div>

    <div class="dualTab">
        <btn class="tabLeft active">TabLeft</btn>
        <btn class="tabRight">TabRight</btn>
    </div>

    <div class="dualTab">
      <btn class="tabLeft">TabLeft</btn>
      <btn class="tabRight active">TabRight</btn>
    </div>

    <h3>Cross</h3>

      <div class="containerCross col-12">
        <span class="cross fas fa-times-circle"/>
      </div>

    <h3>ActionLabelIcon</h3>

      <div class="actionLabelIcon">
        <span class="illustration btn fas fa-filter"/>
        <div class="text">petit mot sympatique</div>
      </div>

  </div>
</div>

<div class="designContainer col-8 offset-2 row">
  <h2>Inputs</h2>
  <div class="col-md-6">

    <h3>Radio // from Bootstrap</h3>
    <div class="radioSelect">
      <div class="form-check">
        <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios1" value="option1" checked>
        <label class="form-check-label" for="exampleRadios1">
          Default radio
        </label>
      </div>
      <div class="form-check">
        <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios2" value="option2">
        <label class="form-check-label" for="exampleRadios2">
          Second default radio
        </label>
      </div>
      <div class="form-check disabled">
        <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios3" value="option3" disabled>
        <label class="form-check-label" for="exampleRadios3">
          Disabled radio
        </label>
      </div>
    </div>

    <h3>Switch</h3>
    <label class="customSwitch">
        <input class="switch-input" type="checkbox"/>
        <span class="switch-label" data-on="+" data-off="-"/>
        <span class="switch-handle"/>
    </label>

    <h3>AmountInput</h3>
    <div class="amountInput input-group flex-nowrap">
      <label class="customSwitch">
        <input class="switch-input" type="checkbox" v-model="incoming"/>
          <span class="switch-label" data-on="+" data-off="-"/>
          <span class="switch-handle"/>
      </label>
        <input id="amount" class="form-control">
    </div>

    <h3>Select with search</h3>
    <div class="selectAutoComplete form-group">
      <label for="category">Category</label>
      <Multiselect
        id="category"
        v-model="categoryId"
        :groups="true"
        :searchable="true"
        :options="categories"
        :noResultsText="$t('NO_RESULT_FOUND')"
        :placeholder="$t('SELECT_CATEGORY')"
      />
    </div>

    </div>
    <div class="col-md-6">

    <h3>Text Input</h3>
    <div class="textInput form-group">
      <label for="textInput">Email address</label>
      <input type="email" class="form-control" id="textInput" placeholder="name@example.com">
    </div>

    <h3>Dark Text Input</h3>
    <div class="darkTextInput form-group">
      <label for="darkTextInput">Email address</label>
      <input type="email" class="form-control" id="darkTextInput" placeholder="name@example.com">
    </div>

    <h3>Number Input</h3>
    <div class="numberInput form-group">
      <label for="textInput">Number</label>
      <input type="number" class="form-control" id="textInput" placeholder="0">
    </div>
  </div>
</div>

<div class="designContainer col-8 offset-2 row">
  <h2>Containers</h2>
  <div class="col-md-6">
    <h3>Number Container</h3>
    <span class="positive">124 221</span>
    <span class="negative">- 124 221</span>

    <h3>Container Header</h3>
    <div class="container header">
      <div class="numberInput form-group">
        <label for="textInput">Number</label>
        <input type="number" class="form-control" id="textInput" placeholder="0">
      </div>
      <div class="numberInput form-group">
        <label for="textInput">Number</label>
        <input type="number" class="form-control" id="textInput" placeholder="0">
      </div>
    </div>

    <h3>Container default</h3>
    <div class="container">
      <div class="numberInput form-group">
        <label for="textInput">Number</label>
        <input type="number" class="form-control" id="textInput" placeholder="0">
      </div>
      <div class="numberInput form-group">
        <label for="textInput">Number</label>
        <input type="number" class="form-control" id="textInput" placeholder="0">
      </div>
    </div>

  </div>
</div>

<h1 class="title">Organisms</h1>
<div class="designContainer col-8 offset-2 row">
  <h2>Navigation</h2>
  <div class="col-md-6">
    <h3>NavBar</h3>
    <div class="navBar">
      <ul class="list-group list-group-horizontal d-flex">
        <li v-if="this.fromPage == 'home' "><button class="navBarBtn btn fas fa-home disabled" :title="$t('HOME')"/></li>
        <li v-else><button v-on:click="goHomePage" class="navBarBtn btn fas fa-home" :title="$t('HOME')"/></li>
        <li v-if="this.fromPage == 'accounts' "><button class="navBarBtn btn fas fa-euro-sign disabled" :title="$t('ACCOUNTS')"/></li>
        <li v-else><button v-on:click="goChooseAccount" class="navBarBtn btn fas fa-euro-sign" :title="$t('ACCOUNTS')"/></li>
        <li v-if="this.fromPage == 'profile' "><button class="navBarBtn btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
        <li v-else><button v-on:click="goProfilePage" class="navBarBtn btn fas fa-cog" :title="$t('PREFERENCES')"/></li>
        <li><button v-on:click="logout" class="navBarBtn btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
      </ul>
    </div>
    <h3>Calendar</h3>
    <div class="row dateNav">
      <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
      <div class="col-8 date-label title">
          <p class="month">April</p>
      </div>
      <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
    </div>
    <div class="row dateNav">
      <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
      <div class="col-8 date-label title positive">
          <p>April</p>
          <p>3 000€ à distribuer</p>
      </div>
      <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
    </div>
    <div class="row dateNav">
      <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
      <div class="col-8 date-label title negative">
          <p>April</p>
          <p>3 000€ à retirer</p>
      </div>
      <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
    </div>
  </div>
</div>

<div class="designContainer col-8 offset-2 row">
  <h2>Budget Data</h2>
  <h3>Budget Table</h3>
  <div class="col-md-6">
    <table class="budgetTable">
      <thead class="masterCategory">
        <tr>
          <th class="col-6">
            <a>Master Category</a>
            <span class="action">
              <button class="illustration btn fas fa-pen"/>
              <button class="illustration btn fas fa-plus"/>
            </span>
          </th>
          <th class="col-3 spent">124</th>
          <th class="col-3">300</th>
        </tr>
      </thead>
      <tbody class="categoryBudget">
        <tr>
          <td class="col-6">
            <a>Enveloppe
              <button class="illustration btn fas fa-pen"/>
            </a>
          </td>
          <td class="col-3">
            <div class="numberInput form-group">
                <input type="text" class="form-control" id="textInput" placeholder="0">
            </div>
          </td>
          <td class="col-3">300</td>
        </tr>
      </tbody>
    </table>
    <h3>MasterCategoryHeader collapsed</h3>
    <table class="budgetTable">
      <thead class="masterCategory collapsed">
        <tr>
          <th class="col-6">
            <a>Master Category</a>
            <span class="action">
              <button class="illustration btn fas fa-pen"/>
              <button class="illustration btn fas fa-angle-double-down"/>
            </span>
          </th>
          <th class="col-3">124</th>
          <th class="col-3">300</th>
        </tr>
      </thead>
    </table>
    <h3>MasterCategoryHeader Edit</h3>
    <table class="budgetTable">
      <thead class="masterCategory edit">
        <tr>
          <th class="col-6">
            <div class="darkTextInput form-group">
                <input type="textInput" class="form-control" id="darkTextInput" placeholder="Master Category">
            </div>
          </th>
          <th class="col-3">
            <span class="illustration fas fa-check"/>
          </th>
          <th class="col-3">
            <span class="illustration fas fa-archive"/>
          </th>
        </tr>
      </thead>
      <tbody class="categoryBudget edit">
        <tr>
          <td class="col-6">
            <div class="textInput form-group">
              <input type="textInput" class="form-control" id="textInput" placeholder="Category">
            </div>
          </td>
          <td class="col-3">
            <span class="illustration fas fa-check"/>
          </td>
          <td class="col-3">
            <span class="illustration fas fa-archive"/>
          </td>
        </tr>
      </tbody>
    </table>
    <h3>Budget Header</h3>
    <table class="budgetTable">
        <thead class="masterCategory collapsed col-6 offset-6">
          <tr>
            <th class="col-6">Alloué</th>
            <th class="col-6">Disponible</th>
          </tr>
        </thead>
      </table>
    <h3>Budget Total Line</h3>
    <table class="budgetTable">
      <thead class="masterCategory collapsed">
        <tr>
          <th class="col-6">Total</th>
          <th class="col-3">124</th>
          <th class="col-3">300</th>
        </tr>
      </thead>
    </table>
  </div>
</div>

</template>
<script lang="ts">
import { defineComponent } from 'vue'
import Multiselect from '@vueform/multiselect'

export default defineComponent({
  name: 'About',
  components: {
    Multiselect
  },
  data () {
    return {
      categories: [
        {
          label: this.$t('DEFAULT'),
          options: [
            { value: 'lala', label: this.$t('I18N_INCOME') },
            { value: 'lili', label: this.$t('I18N_TRASNFERT') }
          ]
        }
      ]
    }
  }
})
</script>
<style lang="less">
  @import "../style/atomic_design/atomic_design.less";
</style>

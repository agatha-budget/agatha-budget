<template>
  <div v-for="element in choices" :key="element.value" class="radioSelect typeSelection">
    <div class="form-check">
      <!-- <input v-if="element.preSelected" class="form-check-input" type="checkbox" :value="element.value" id="flexCheckChecked" checked v-model="result"> -->
      <input v-if="element.preSelected" class="form-check-input" type="checkbox" :id="element.value" :value="element.value" v-model="result" checked>
      <input v-else class="form-check-input" type="checkbox" :id="element.value" :value="element.value" v-model="result">
      <label class="form-check-label" :for="element.value">{{ element.label }}</label>
    </div>
  </div>
</template>

<script lang="ts">
import { ChoiceElement } from '@/model/model'
import { defineComponent } from 'vue'

interface CheckboxSelectData {
  result: string[];
}

export default defineComponent({
  name: 'CheckboxSelect',
  beforeCreate: function () {
    console.log('before')
  },
  created: function () {
    console.log('hey !')
    this.$props.choices.forEach((element: { label: string; value: string; preSelected: boolean }) => {
      if (element.preSelected) {
        this.result.push(element.value)
      }
      console.log(this.result)
    })
  },
  watch: {
    result: function () {
      console.log(this.result)
      this.$emit('hadSelection', this.result)
    }
  },
  props: {
    choices: {
      type: Object as () => ChoiceElement[], // (max one element is preSelected)
      required: true
    }
  },
  data (): CheckboxSelectData {
    return {
      result: []
    }
  },
  methods: { }
})
</script>

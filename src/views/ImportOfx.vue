<template>
  <div :class="this.$store.state.css">
    <div class="importPage">
      <div class="importOfx col-12 offset-0 col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-lg-4 offset-lg-4">
          <button v-on:click="goBackAccountPage()" class="goBackAccountPage icon-action-undo" :title="$t('GO_BACK_TO_ACCOUNT')"/>
          <div v-if="!fileSelected">
            <p>{{ $t('TEXT_IMPORT') }}</p>
            <div class="ofxForm">
              <input id="importOfxFile" type="file" @change="onFileChange($event)" class="inputOfx" />
              <label for="importOfxFile" class="col-12">{{ $t('SELECT_FILE') }}</label>
            </div>
          </div>
          <div v-else>
            <p>{{ $t('TEXT_IMPORTED') }}</p>
            <button v-on:click="importOfxFile()" class="btnOfx">{{ $t('IMPORT') }}</button>
          </div>
      </div>
      <NavMenu class="col-12 offset-0 col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-lg-4 offset-lg-4"/>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import NavMenu from '@/components/NavigationMenu.vue'
import OperationService from '@/services/OperationService'
import router, { RouterPages } from '@/router'

interface ImportOfxData {
    fileOfx: any;
}

export default defineComponent({
  name: 'ImportOfx',
  components: {
    NavMenu
  },
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  computed: {
    fileSelected (): boolean {
      if (this.fileOfx === '') {
        return false
      } else {
        return true
      }
    }
  },
  data (): ImportOfxData {
    return {
      fileOfx: ''
    }
  },
  methods: {
    onFileChange (event: { target: { files: any[] } }) {
      const file = event.target.files[0]
      this.fileOfx = file
    },
    importOfxFile () {
      const fr = new FileReader()
      fr.readAsText(this.fileOfx, 'UTF-8')
      fr.onload = (evt) => {
        if (evt.target) {
          console.log(evt.target.result)
          if (evt.target.result) {
            const ofx: string = evt.target.result.toString()
            OperationService.openAndReadOfxFile(this.$store, this.accountId, ofx)
          }
        }
      }
      fr.onerror = (evt) => {
        console.error('Failed to read this file')
      }
      router.push({ path: RouterPages.account, query: { accountId: this.$props.accountId } })
    },
    goBackAccountPage () {
      router.push({ path: RouterPages.account, query: { accountId: this.$props.accountId } })
    }
  }
})
</script>

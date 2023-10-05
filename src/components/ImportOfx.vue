<template>
  <div class="container header">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeImport"/>
    </div>
    <div class="importPage">
      <div v-if="fileImported">
        <p class="text">{{ this.nbOperationImported }}{{ $t('IMPORTED_OPERATION') }}</p>
      </div>
      <div v-else>
        <div v-if="fileSelected">
          <p class="text">{{ $t('FILE_IS_SELECTED') }}</p>
          <button v-on:click="importOfxFile()" class="actionButton">{{ $t('IMPORT') }}</button>
        </div>
        <div v-else>
          <div class="ofxForm">
            <input id="importOfxFile" type="file" @change="onFileChange($event)" class="fileInput" />
            <label for="importOfxFile" class="actionButton">{{ $t('SELECT_FILE') }}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import OperationService from '@/services/OperationService'

interface ImportOfxData {
    fileOfx: Blob | undefined;
    fileImported: boolean;
    nbOperationImported: string;
}

export default defineComponent({
  name: 'ImportOfx',
  components: {},
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  computed: {
    fileSelected (): boolean {
      if (this.fileOfx === undefined) {
        return false
      } else {
        return true
      }
    }
  },
  data (): ImportOfxData {
    return {
      fileOfx: undefined,
      fileImported: false,
      nbOperationImported: '0'
    }
  },
  emits: ['closeImport'],
  methods: {
    onFileChange (event: { target: { files: Blob[] } }) {
      const file = event.target.files[0]
      this.fileOfx = file
    },
    async importOfxFile () {
      if (this.fileOfx !== undefined) {
        const fr = new FileReader()
        fr.readAsText(this.fileOfx, 'UTF-8')
        fr.onload = async (evt) => {
          if (evt.target) {
            console.log(evt.target.result)
            if (evt.target.result) {
              const ofx: string = evt.target.result.toString()
              this.nbOperationImported = await OperationService.importOfxFile(this.$store, this.accountId, ofx)
              this.fileImported = true
            }
          }
        }
        fr.onerror = () => {
          console.error('Failed to read this file')
        }
      }
    },
    closeImport () {
      this.$emit('closeImport')
    }
  }
})
</script>

<template>
    <FilterCmpt v-if="filterBloc" @close-filter="closeFilter" @filtering-category="filter"/>
    <div class="operationList">
    <template v-for="operation in operations" :key="operation">
        <OperationForm v-if="operation.id === editing" class="operationForm inlineForm container" @close-update="closeUpdate" :accountId="accountId" :operation="operation"/>
        <!-- Operation without daugther -->
        <div v-else-if="operation.daughters.length == 0"  v-on:click="setAsEditing(operation)" class="operationListItem operation">
        <div class="date">{{ $d(getDayAsDate(operation.day), "day") }}</div>
        <span class="illustration btn fas fa-pen action edit" :title="$t('EDIT')"></span>
        <div class="category" :class="getClassDependingCategory(operation)">
            {{ getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
        </div>
        <div class="amount" :class="getClassDependingOnAmount(operation.amount)">
            {{ centsToEurosDisplay(operation.amount) }} €
        </div>
        <span v-if="operation.pending" class="pending illustration btn fas fa-hourglass-half"></span>
        <div class="memo">
            <div>{{ operation.memo }}</div>
        </div>
        </div>
        <!-- Operation with daugther -->
        <div v-else class="operationListItem hasDaughter operation" v-on:click="setAsEditing(operation)">
        <div class="date">{{ $d(getDayAsDate(operation.day), "day") }}</div>
        <button class="illustration btn fas fa-pen action edit" :title="$t('EDIT')" />
        <div class="memo">{{ operation.memo }}</div>
        <div class="amount" :class="getClassDependingOnAmount(operation.amount)">
            {{ centsToEurosDisplay(operation.amount) }} €
        </div>
        <span v-if="operation.pending" class="pending illustration btn fas fa-hourglass-half"></span>
        <hr class="separator"/>
        <div class="daughters">
        <template v-for="daughter in operation.daughters" :key="daughter">
            <div class=" daughter">
            <div class="daughterCategory category" :class="getClassDependingCategoryDaughter(daughter.categoryId)">
                {{ getCategoryById(daughter.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
            </div>
            <div class="amount" :class="getClassDependingOnAmount(daughter.amount)">
                {{ centsToEurosDisplay(daughter.amount) }} €
            </div>
            <div class="daughterMemo">{{ (daughter.memo === 'null') ? '' : daughter.memo }}</div>
            </div>
        </template>
        </div>
        </div>
    </template>
    </div>
    <div v-if="operations.length === maxDisplayed">
    <button v-on:click="increaseMaxDisplayed" class="actionButton">{{ $t("DISPLAY_MORE") }}</button>
    </div>
    <div v-else>
      <Loader class="loader"/>
    </div>
    </div>
</template>


import { redirectOnApiError } from '@/router'
import axios from 'axios'

export function defaultErrorHandler(e: unknown) {
    if (axios.isAxiosError(e)) {
        redirectOnApiError(e)
    }
    return Error("error while contacting the DB")
}

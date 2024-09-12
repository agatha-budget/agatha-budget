import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
    site: 'https://doc.agatha-budget.fr',
    base: '/',
    integrations: [
        starlight({
            title: 'Documentation',
            // Set English as the default language for this site.
            defaultLocale: 'root',
            locales: {
                // English docs in `src/content/docs/en/`
                root: {
                    label: 'English',
                    lang: 'en'
                },
                // French docs in `src/content/docs/fr/`
                fr: {
                    label: 'Français',
                    lang: 'fr'
                }
            },
            social: {
                github: 'https://github.com/agatha-budget/agatha-budget',
            },
        }),
    ],
});

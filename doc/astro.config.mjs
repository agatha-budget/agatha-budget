import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
    site: 'https://agatha-budget.github.io',
    base: '/agatha-budget',
    integrations: [
        starlight({
            title: 'Documentation',
            // Set English as the default language for this site.
            defaultLocale: 'en',
            locales: {
                // English docs in `src/content/docs/en/`
                en: {
                label: 'English',
                },
                // French docs in `src/content/docs/fr/`
                fr: {
                label: 'Français',
                }
            },
            social: {
                github: 'https://github.com/agatha-budget/agatha-budget',
            },
            sidebar: [
                {
                    label: 'Guides',
                    items: [
                        // Each item here is one entry in the navigation menu.
                        { label: 'Example Guide', link: '/guides/example/' },
                    ],
                },
                {
                    label: 'Reference',
                    autogenerate: { directory: 'reference' },
                },
            ],
        }),
    ],
});

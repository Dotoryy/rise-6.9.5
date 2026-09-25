package com.alan.clients.ui.palette;

import java.util.List;

interface SuggestionProvider {
    default void a(SuggestionContext suggestionContext) {
    }

    List<Suggestion> b(SuggestionContext suggestionContext);
}

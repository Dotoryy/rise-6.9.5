package com.alan.clients.ui.menu.impl.intro;

import java.util.ArrayList;

class IntroSequenceLines extends ArrayList<IntroLine> {
    final IntroSequence aCp;

    IntroSequenceLines(IntroSequence var1) {
        this.aCp = var1;
        this.add(new IntroLine("Around the release of Rise 5.3, the team behind Rise began to envision a new project", 10000L));
        this.add(new IntroLine("This project came to be known as Rise 6.", 5000L));
        this.add(new IntroLine("", 500L));
        this.add(new IntroLine("Today", 2000L));
        this.add(new IntroLine("Thousands of development hours from some of the most talented minds in the community", 7500L));
        this.add(new IntroLine("Hundreds of hours of testing and feedback from dedicated beta testers", 6000L));
        this.add(new IntroLine("", 500L));
        this.add(new IntroLine("It's ready", 2000L));
        this.add(new IntroLine("", 500L));
        this.add(new IntroLine("With the highest quality features ever seen in a client", 5000L));
        this.add(new IntroLine("The highest frame rates ever seen in a Minecraft client of any kind", 6000L));
        this.add(new IntroLine("The smoothest and most meticulously polished user experience ever", 6000L));
        this.add(new IntroLine("And so many more powerful and unique features in development for updates in the very near future", 9000L));
        this.add(new IntroLine("We're so incredibly proud to show you the future of Rise.", 6000L));
        this.add(new IntroLine("", 500L));
        this.add(new IntroLine("Welcome to the pioneer of the next generation of Minecraft clients.", 7000L));
    }
}

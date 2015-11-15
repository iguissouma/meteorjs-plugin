Meteor JS Template Plugin
===========================

An IntelliJ plugin containing Meteor JS live templates and some other cool features!
- Add JS/HTML File templates for Meteor.
- Add Meteor JS Boilerplate by Differential.
- Add Toggle action between HTML and JS|COFFEE files [Alt][8]
- Add Live templates for Meteor JS framework.

This plugin works for the following JetBrains products:
- IntelliJ IDEA
- WebStorm

[Youtube video - Plugin in action](https://www.youtube.com/watch?v=ALyUzDoN-1A)

Feel free to let me know what else you want added via the [issues](https://github.com/iguissouma/meteorjs-plugin/issues).

Suggestions, feedback and other comments welcome via [@iguissouma](https://twitter.com/iguissouma) on Twitter.

### Installation (in 3 easy steps)

To install the plugin open your editor (IntelliJ) and hit:

1) `File > Settings > Plugins` and click on the `Browse repositories` button.

2) Look for `Meteor JS Template` the right click and select `Download plugin`.

3) Finally hit the `Apply` button, agree to restart your IDE and you're all done!

### Usage

To use the plugin, open an editor, and start typing `m`, followed by pressing CMD+J. A list of templates will show up.

## What's new

Since 0.6.1

- Automatically navigate to created file
- Add minc livetemplate: Include Blaze template

Since 0.6.0

- Blaze Updates: New template callbacks for liveTemplates and JS File Template

Since 0.5.0

Add option for file creation when toggling between files

Since 0.4.0

- JDK 6 compliant
- Fix mevn live template

Since 0.3.0

- Add popup file chooser when there's more than one file and toggling between HTML/JS</li>
- Add Toggle action between HTML and JS|COFFEE files([Alt][8])

Since 0.2.0

- Add JS/HTML File templates for Meteor

Since 0.1.0

- Initial version


### HTML Live templates
__mtp__
```html
<template name='main'>
	...
</template>
```
__mea__
```handlebars
{{#each player}}
	...
{{/each}}
```

### JS live templates
__mcol__
```js
Players = new Mongo.Collection('players');
```

## Todo's

 *

## Contributing

Got some neat Meteor snippets or feature you want to share?
fork this repository and send me a pull request or just create an issue!

### License

MeteorJS Template - IntelliJ Plugin is open-sourced software licensed under the [MIT license](http://opensource.org/licenses/MIT).


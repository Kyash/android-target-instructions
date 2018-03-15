# Android Target Instructions

[![JitPack](https://jitpack.io/v/Kyash/android-target-instructions.svg)](https://jitpack.io/#Kyash/android-target-instructions)

Make easy to implement the instructions feature.

## Download

### Project build.gradle

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

### App build.gradle

```groovy
dependencies {
    ...
    compile 'com.github.Kyash:android-target-instructions:LATEST_VERSION'
}
```

`LATEST_VERSION` is  [![JitPack](https://jitpack.io/v/Kyash/android-target-instructions.svg)](https://jitpack.io/#Kyash/android-target-instructions)

## Usage

```kotlin
val target1 = SimpleTarget.Builder(this@MainActivity).setCoordinate(binding.fab)
    .setTitle("Floating Action Button")
    .setRadius(100f)
    .setDescription("This is the floating action button.")
    .build()

val target2 = SimpleTarget.Builder(this@MainActivity).setCoordinate(binding.firstText)
    .setTitle("First text")
    .setDescription("This is the first text.")
    .setHighlightPadding(R.dimen.simple_hightlight_padding)
    .setListener(object : Target.OnStateChangedListener {
        override fun onClosed() {
            binding.scrollview.smoothScrollBy(0, 10000)
        }
    })
    .build()

val target3 = SimpleTarget.Builder(this@MainActivity).setCoordinate(binding.secondText)
    .setTitle("Second text")
    .setDescription("This is the second text.")
    .setMessageLayoutResId(R.layout.layout_instruction_simple_message_black)
    .setHighlightHorizontalPadding(R.dimen.space_minus_16dp)
    .setStartDelayMillis(200L)
    .build()

TargetInstructions.with(this@MainActivity)
    .setTargets(arrayListOf(target1, target2, target3))
    .start()
```

## Contributing
We are always welcome your contribution!
If you find a bug or want to add new feature, please raise issue.

## License

```
Copyright 2018 Kyash

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```